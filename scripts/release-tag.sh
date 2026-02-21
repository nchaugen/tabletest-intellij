#!/bin/bash
set -euo pipefail

GRADLE_PROPERTIES="gradle.properties"

if ! git rev-parse --show-toplevel >/dev/null 2>&1; then
    echo "Error: not inside a git repository"
    exit 1
fi

repo_root=$(git rev-parse --show-toplevel)
cd "$repo_root"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "Error: $GRADLE_PROPERTIES not found in repo root"
    exit 1
fi

# Ensure a clean working tree before making changes
if ! git diff --quiet || ! git diff --cached --quiet; then
    echo "Error: working tree is not clean. Commit or stash changes before releasing."
    git status --short
    exit 1
fi

current_branch=$(git rev-parse --abbrev-ref HEAD)
if [ "$current_branch" != "main" ]; then
    echo "Error: releases must be created from main (current: $current_branch)"
    exit 1
fi

if ! git remote get-url origin >/dev/null 2>&1; then
    echo "Error: remote 'origin' is not configured"
    exit 1
fi

# Read current version from gradle.properties
current_version=$(grep "^pluginVersion" "$GRADLE_PROPERTIES" | sed 's/.*= *//')
if [ -z "$current_version" ]; then
    echo "Error: pluginVersion not found in $GRADLE_PROPERTIES"
    exit 1
fi

if [ -z "$1" ]; then
    # No argument - use version from gradle.properties
    version="$current_version"
    echo "Using version from gradle.properties: $version"
    updated_files=false
else
    # Argument provided - update gradle.properties
    version="$1"
    if [ "$version" = "$current_version" ]; then
        echo "Version unchanged ($version); using current version from gradle.properties"
        updated_files=false
    else
        echo "Updating version from $current_version to $version"

        if [[ "$OSTYPE" == "darwin"* ]]; then
            sed -i '' "s/^pluginVersion *=.*/pluginVersion = $version/" "$GRADLE_PROPERTIES"
        else
            sed -i "s/^pluginVersion *=.*/pluginVersion = $version/" "$GRADLE_PROPERTIES"
        fi
        echo "Updated gradle.properties"

        updated_files=true
    fi
fi

if ! [[ "$version" =~ ^[0-9]+\\.[0-9]+\\.[0-9]+([.-][0-9A-Za-z.-]+)?$ ]]; then
    echo "Error: version '$version' does not look like SemVer (e.g. 1.2.3 or 1.2.3-rc.1)"
    exit 1
fi

tag="v$version"

# Check if tag already exists
if git rev-parse "$tag" >/dev/null 2>&1; then
    echo "Error: Tag $tag already exists"
    exit 1
fi

# Check remote state unless explicitly skipped
if [ "${SKIP_REMOTE_CHECKS:-}" != "1" ]; then
    echo "Fetching origin/main ..."
    git fetch origin main --tags

    remote_head=$(git rev-parse origin/main)
    local_head=$(git rev-parse HEAD)
    if [ "$remote_head" != "$local_head" ]; then
        counts=$(git rev-list --left-right --count origin/main...HEAD)
        behind=$(echo "$counts" | awk '{print $1}')
        ahead=$(echo "$counts" | awk '{print $2}')
        echo "Error: local main is not in sync with origin/main (ahead: $ahead, behind: $behind)"
        echo "Please push/pull so local main matches origin/main before releasing."
        exit 1
    fi

    if git ls-remote --tags origin "$tag" | grep -q "$tag"; then
        echo "Error: Tag $tag already exists on origin"
        exit 1
    fi
fi

# If we updated gradle.properties, ensure it's the only change
if [ "$updated_files" = true ]; then
    changed_files=$(git status --porcelain | awk '{print $2}')
    if [ "$changed_files" != "$GRADLE_PROPERTIES" ]; then
        echo "Error: unexpected changes detected. Only $GRADLE_PROPERTIES should be modified."
        git status --short
        exit 1
    fi
fi

# Run tests unless explicitly skipped
if [ "${SKIP_TESTS:-}" != "1" ]; then
    echo ""
    echo "Running ./gradlew check ..."
    ./gradlew check
    if ! git diff --quiet || ! git diff --cached --quiet; then
        echo "Error: working tree changed after running tests. Please clean generated files."
        git status --short
        exit 1
    fi
fi

# Show current status
echo ""
echo "Ready to create tag: $tag"
echo ""
git status --short

echo ""
read -p "Release $tag? (y/n) " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]; then
    if [ "$updated_files" = true ]; then
        git add "$GRADLE_PROPERTIES"
        git commit -m "chore: bump version to $version"
    fi

    git tag "$tag"
    echo ""
    echo "Created tag: $tag"
    echo ""
    echo "Pushing to remote..."
    git push origin main && git push origin "$tag"
    echo ""
    echo "Successfully pushed tag $tag - release workflow will start shortly"
else
    if [ "$updated_files" = true ]; then
        git checkout "$GRADLE_PROPERTIES"
        echo "Reverted changes"
    fi
    echo "Aborted"
fi

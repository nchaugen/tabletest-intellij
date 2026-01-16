#!/bin/bash
set -e

GRADLE_PROPERTIES="gradle.properties"
CHANGELOG="CHANGELOG.md"

# Read current version from gradle.properties
current_version=$(grep "^pluginVersion=" "$GRADLE_PROPERTIES" | cut -d'=' -f2)

if [ -z "$1" ]; then
    # No argument - use version from gradle.properties
    version="$current_version"
    echo "Using version from gradle.properties: $version"
    updated_files=false
else
    # Argument provided - update gradle.properties and changelog
    version="$1"
    echo "Updating version from $current_version to $version"

    if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i '' "s/^pluginVersion=.*/pluginVersion=$version/" "$GRADLE_PROPERTIES"
    else
        sed -i "s/^pluginVersion=.*/pluginVersion=$version/" "$GRADLE_PROPERTIES"
    fi
    echo "Updated gradle.properties"

    # Update changelog - insert version header after [Unreleased]
    release_date=$(date +%Y-%m-%d)
    version_header="## [$version] - $release_date"

    if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i '' "s/^## \[Unreleased\]$/## [Unreleased]\n\n$version_header/" "$CHANGELOG"
    else
        sed -i "s/^## \[Unreleased\]$/## [Unreleased]\n\n$version_header/" "$CHANGELOG"
    fi
    echo "Updated CHANGELOG.md with $version_header"

    updated_files=true
fi

tag="v$version"

# Check if tag already exists
if git rev-parse "$tag" >/dev/null 2>&1; then
    echo "Error: Tag $tag already exists"
    exit 1
fi

# Show current status
echo ""
echo "Ready to create tag: $tag"
echo ""
git status --short

echo ""
read -p "Create tag $tag? (y/n) " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]; then
    if [ "$updated_files" = true ]; then
        git add "$GRADLE_PROPERTIES" "$CHANGELOG"
        git commit -m "Bump version to $version"
    fi

    git tag "$tag"
    echo ""
    echo "Created tag: $tag"
    echo ""
    echo "To push the tag and trigger the release workflow:"
    echo "  git push origin main && git push origin $tag"
else
    if [ "$updated_files" = true ]; then
        git checkout "$GRADLE_PROPERTIES" "$CHANGELOG"
        echo "Reverted changes"
    fi
    echo "Aborted"
fi

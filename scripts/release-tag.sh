#!/bin/bash
set -e

GRADLE_PROPERTIES="gradle.properties"

# Read current version from gradle.properties
current_version=$(grep "^pluginVersion=" "$GRADLE_PROPERTIES" | cut -d'=' -f2)

if [ -z "$1" ]; then
    # No argument - use version from gradle.properties
    version="$current_version"
    echo "Using version from gradle.properties: $version"
else
    # Argument provided - update gradle.properties
    version="$1"
    echo "Updating version from $current_version to $version"

    if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i '' "s/^pluginVersion=.*/pluginVersion=$version/" "$GRADLE_PROPERTIES"
    else
        sed -i "s/^pluginVersion=.*/pluginVersion=$version/" "$GRADLE_PROPERTIES"
    fi

    echo "Updated gradle.properties"
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
    if [ -n "$1" ]; then
        # If we updated the version, commit the change first
        git add "$GRADLE_PROPERTIES"
        git commit -m "Bump version to $version"
    fi

    git tag "$tag"
    echo ""
    echo "Created tag: $tag"
    echo ""
    echo "To push the tag and trigger the release workflow:"
    echo "  git push origin main && git push origin $tag"
else
    if [ -n "$1" ]; then
        # Revert the gradle.properties change
        git checkout "$GRADLE_PROPERTIES"
        echo "Reverted gradle.properties"
    fi
    echo "Aborted"
fi

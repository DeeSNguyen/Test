#!/usr/bin/env bash
# Simple setup script for offline test environment
# This script installs OpenJDK and pre-downloads Gradle dependencies.
# It assumes network access is available when executed.

set -e

# Install Java if not present
if ! command -v java >/dev/null 2>&1; then
  sudo apt-get update
  sudo apt-get install -y openjdk-11-jdk
fi

# Pre-download Gradle wrapper distribution and project dependencies
./gradlew --no-daemon --refresh-dependencies tasks >/dev/null

echo "Setup complete"

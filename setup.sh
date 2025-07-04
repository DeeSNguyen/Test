#!/usr/bin/env bash
set -euxo pipefail     # fail hard, fail fast, print everything

# 1. System update & required packages
apt-get update -qq
apt-get install -yqq maven

# 2. Verify the install (good for debugging)
mvn   -v

# 3. Configure Maven to use the Codex proxy
mkdir -p ~/.m2
cat > ~/.m2/settings.xml <<'EOS'
<settings>
  <proxies>
    <proxy>
      <id>codexProxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy</host>
      <port>8080</port>
    </proxy>
  </proxies>
</settings>
EOS

# 4. Pre-download all project dependencies while the network is still up
#    Replace "pom.xml" with the path to your own POM if needed
mvn --batch-mode dependency:go-offline

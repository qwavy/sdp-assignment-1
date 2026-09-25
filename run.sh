#!/bin/bash
set -e

JAVA_CMD="/opt/homebrew/opt/openjdk/bin/java"
JAVAC_CMD="/opt/homebrew/opt/openjdk/bin/javac"

if ! command -v "$JAVAC_CMD" &> /dev/null; then
    JAVA_CMD="java"
    JAVAC_CMD="javac"
fi

echo "Compiling Java source files..."
mkdir -p bin

$JAVAC_CMD -d bin \
  src/main/java/com/sdp/drone/model/*.java \
  src/main/java/com/sdp/drone/legacy/*.java \
  src/main/java/com/sdp/drone/director/*.java \
  src/main/java/com/sdp/drone/*.java \
  src/test/java/com/sdp/drone/*.java

echo ""
echo "=== EXECUTING MAIN APPLICATION ==="
$JAVA_CMD -cp bin com.sdp.drone.Main

echo ""
echo "=== EXECUTING AUTOMATED TEST SUITE ==="
$JAVA_CMD -cp bin com.sdp.drone.DroneMissionTest

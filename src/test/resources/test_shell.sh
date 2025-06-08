#!/bin/bash
export CODECRAFTERS_TEST=1
SHELL_INTERPRETER=/mnt/c/Development/Projects/codecrafters-shell-java/my_shell.sh  # Adjust path to your shell

pass() {
  echo "[✓] $1"
}

fail() {
  echo "[✗] $1"
  echo "   Expected: '$2'"
  echo "   Got     : '$3'"
}

test_case() {
  local input="$1"
  local expected="$2"
  local actual
  actual=$(echo "$input" | $SHELL_INTERPRETER 2>/dev/null | tail -n +2)

  if [ "$actual" = "$expected" ]; then
    pass "$input"
  else
    fail "$input" "$expected" "$actual"
  fi
}

# Basic quoting and escaping
test_case "echo example\\ \\ test" "example  test"
test_case "echo 'quoted string'" "quoted string"
test_case "echo \"double quoted string\"" "double quoted string"
test_case "echo 'nested \"quotes\" test'" 'nested "quotes" test'
test_case "echo \"escaped \\\"quotes\\\" test\"" 'escaped "quotes" test'

# Backslashes inside/outside quotes
test_case "echo a\\nb" "anb"
test_case "echo 'a\\nb'" "a\\nb"

# File creation and globbing test setup
mkdir -p /tmp/qux
echo -n "orange" > /tmp/qux/f\\n70
echo -n "mango" > /tmp/qux/f\\91
echo -n "banana" > "/tmp/qux/f'\\'56"

test_case "cat /tmp/qux/f\\n70 /tmp/qux/f\\91 \"/tmp/qux/f'\\'56\"" "orangemangobanana"

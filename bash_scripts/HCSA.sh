#!/bin/bash

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")"/.. && pwd)"
SRC_DIR="$PROJECT_ROOT/src"
CLASS_DIR="$PROJECT_ROOT/target/classes"

BASH_SCRIPTS_DIR="$PROJECT_ROOT/bash_scripts"
INPUT_BASE_DIR="/home/uran/Documents/GitHub/Book.Scanning.Dataset/instances"

OUTPUT_BASE_DIR="$PROJECT_ROOT/output_instances/HCSA"
RESULTS_FILE="$PROJECT_ROOT/results/HCSA.csv"

mkdir -p "$OUTPUT_BASE_DIR" "$(dirname "$RESULTS_FILE")"
mkdir -p "$CLASS_DIR"

echo "Compiling Java code..."
find "$SRC_DIR" -name "*.java" > "$SRC_DIR/sources.txt"
javac -d "$CLASS_DIR" @"$SRC_DIR/sources.txt"

if [ $? -ne 0 ]; then
    echo "Compilation failed! Exiting."
    rm "$SRC_DIR/sources.txt"
    exit 1
fi

rm "$SRC_DIR/sources.txt"

echo "instance_path,score" > "$RESULTS_FILE"

find "$INPUT_BASE_DIR" -type f -name "*.txt" | while read INPUT_FILE; do
    RELATIVE_PATH="${INPUT_FILE#$INPUT_BASE_DIR/}"

    OUTPUT_PATH="$OUTPUT_BASE_DIR/${RELATIVE_PATH%.txt}"
    OUTPUT_DIR=$(dirname "$OUTPUT_PATH")

    mkdir -p "$OUTPUT_DIR"

    echo "Processing $RELATIVE_PATH..."

    # Debug: Print paths
    echo "Input: $INPUT_FILE"
    echo "Output: $OUTPUT_PATH"

    OUTPUT=$(java -cp "$CLASS_DIR" Main "$INPUT_FILE" "$OUTPUT_PATH" 5 200 2>&1)
    echo "$OUTPUT"  # Display Java output for debugging

    # Check if output file was created
    if [ ! -f "$OUTPUT_PATH" ]; then
        echo "Warning: Output file not created: $OUTPUT_PATH"
    fi

    SCORE=$(echo "$OUTPUT" | grep -oP 'Best score: \K\d+')

    echo "\"$RELATIVE_PATH\",$SCORE" >> "$RESULTS_FILE"
    echo "Done with $RELATIVE_PATH"
    echo ""
done

echo "All instances processed! Results saved to $RESULTS_FILE."
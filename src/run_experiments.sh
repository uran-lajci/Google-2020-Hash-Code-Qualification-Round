#!/bin/bash

# Define paths
INPUT_DIR="/home/uran/Documents/GitHub/Book.Scanning.Dataset/instances/synthetic"
OUTPUT_DIR="../output_instances"
RESULTS_FILE="results.csv"

# Create output directory if it doesn't exist
mkdir -p "$OUTPUT_DIR"

# CSV header
echo "instance,score,time_ms" > "$RESULTS_FILE"

# Loop through synthetic_1 to synthetic_139
for i in {1..139}; do
  INPUT_FILE="$INPUT_DIR/synthetic_$i.txt"
  OUTPUT_FILE="$OUTPUT_DIR/synthetic_$i"

  echo "Processing $INPUT_FILE..."

  # Run the Java program and capture output
  OUTPUT=$(java Main "$INPUT_FILE" "$OUTPUT_FILE" 1 2>&1)

  # Extract score and time using robust pattern matching
  SCORE=$(echo "$OUTPUT" | grep -oP 'Best score: \K\d+')
  TIME_MS=$(echo "$OUTPUT" | grep -oP 'in \K\d+')

  # Log results to CSV
  echo "$i,$SCORE,$TIME_MS" >> "$RESULTS_FILE"

  echo "Done with synthetic_$i"
done

echo "All instances processed! Results saved to $RESULTS_FILE."
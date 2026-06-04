import os
import sys
import time
import random
import requests
from pathlib import Path

API_URL = "https://models.github.ai/inference/chat/completions"
MODEL_NAME = "gpt-4o"
MAX_TOKENS = 16000
TEMPERATURE = 0.1

GITHUB_TOKEN = os.environ.get("GITHUB_TOKEN")
if not GITHUB_TOKEN:
    print("❌ GITHUB_TOKEN not found", file=sys.stderr)
    sys.exit(1)

headers = {
    "Authorization": f"Bearer {GITHUB_TOKEN}",
    "Content-Type": "application/json"
}

system_prompt = {
    "role": "system",
    "content": (
        "You are an expert Kotlin/Android developer. "
        "Improve the provided code for quality, performance, maintainability, and null‑safety. "
        "Fix any bugs, add missing annotations, and convert to idiomatic Kotlin where possible. "
        "Return ONLY the improved code – no explanations, no extra text."
    )
}

def improve_file(file_path, retries=3):
    """Send file to API with exponential backoff on 429 errors."""
    with open(file_path, "r", encoding="utf-8") as f:
        original_code = f.read()

    payload = {
        "model": MODEL_NAME,
        "messages": [
            system_prompt,
            {"role": "user", "content": original_code}
        ],
        "max_tokens": MAX_TOKENS,
        "temperature": TEMPERATURE
    }

    for attempt in range(retries):
        try:
            resp = requests.post(API_URL, headers=headers, json=payload)
            if resp.status_code == 429:
                # Rate limited – wait and retry
                wait = (2 ** attempt) + random.uniform(0, 1)
                print(f"⚠️ Rate limited on {file_path}. Retrying in {wait:.2f}s...")
                time.sleep(wait)
                continue
            resp.raise_for_status()
            data = resp.json()
            improved = data["choices"][0]["message"]["content"]

            if improved and improved != original_code:
                with open(file_path, "w", encoding="utf-8") as f:
                    f.write(improved)
                print(f"✅ Updated: {file_path}")
            else:
                print(f"⏭️  No change: {file_path}")
            return True

        except Exception as e:
            print(f"❌ Error on attempt {attempt+1} for {file_path}: {e}", file=sys.stderr)
            if attempt == retries - 1:
                return False
            time.sleep(2 ** attempt)
    return False

kotlin_files = list(Path(".").rglob("*.kt"))
print(f"📁 Found {len(kotlin_files)} Kotlin files")

# Process files with a generous delay (6 seconds) to stay within free tier limits.
for i, file in enumerate(kotlin_files):
    print(f"Processing ({i+1}/{len(kotlin_files)}): {file}")
    improve_file(file)
    # Base delay of 6 seconds + small random jitter to avoid burstiness
    delay = 6 + random.uniform(0, 2)
    print(f"Sleeping for {delay:.2f}s before next file...")
    time.sleep(delay)

print("🎉 AI batch edit finished.")

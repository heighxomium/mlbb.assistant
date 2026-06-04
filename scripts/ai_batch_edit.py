import os
import json
import sys
import time
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

def improve_file(file_path):
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

    try:
        resp = requests.post(API_URL, headers=headers, json=payload)
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
        print(f"❌ Error processing {file_path}: {e}", file=sys.stderr)
        return False

kotlin_files = list(Path(".").rglob("*.kt"))
print(f"📁 Found {len(kotlin_files)} Kotlin files")

for file in kotlin_files:
    improve_file(file)
    time.sleep(0.5)

print("🎉 AI batch edit finished.")

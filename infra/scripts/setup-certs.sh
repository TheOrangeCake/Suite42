#!/usr/bin/env bash
set -e

CERT_DIR="infra/nginx"
CERT_FILE="$CERT_DIR/localhost.pem"
KEY_FILE="$CERT_DIR/localhost-key.pem"

if [ -f "$CERT_FILE" ] && [ -f "$KEY_FILE" ]; then
  echo "✅ Certificats déjà présents"
  exit 0
fi

echo "🔐 Certificats absents, génération en cours..."

# Vérifier mkcert
if ! command -v mkcert >/dev/null 2>&1; then
  echo "❌ mkcert n'est pas installé"
  echo "➡️ Installe-le avec : sudo apt install mkcert libnss3-tools"
  exit 1
fi

# Installer la CA locale si besoin
mkcert -install

# Générer les certifs
cd "$CERT_DIR"
mkcert -cert-file localhost.pem -key-file localhost-key.pem localhost 127.0.0.1 ::1

echo "✅ Certificats générés avec succès"
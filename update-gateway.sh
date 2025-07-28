#!/bin/bash

# Étape 1 : Build du front
echo "🔧 Construction du front..."
cd front || exit 1
npm install
npm run build || { echo "❌ Échec du build front"; exit 1; }
cd ..

# Étape 2 : Nettoyage de l'ancien build
echo "🧹 Nettoyage des anciens fichiers statiques..."
rm -rf gateway-service/src/main/resources/static
mkdir -p gateway-service/src/main/resources/static

# Étape 3 : Copie du build dans le Gateway
echo "📦 Copie du front dans le gateway..."
cp -r front/dist/* gateway-service/src/main/resources/static/

# Étape 4 : Rebuild du service gateway
echo "🐳 Reconstruction de l'image Docker du gateway..."
docker-compose build --no-cache gateway-service || { echo "❌ Échec du build Docker"; exit 1; }

# Étape 5 : Redémarrage du service
echo "🚀 Redémarrage du service gateway..."
docker-compose up -d gateway-service || { echo "❌ Échec du démarrage du service"; exit 1; }

echo "✅ Mise à jour terminée avec succès !"


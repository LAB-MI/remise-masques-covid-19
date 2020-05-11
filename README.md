# Covid-19 : Remise des masques pour le grand public

Application destinée aux entités remettantes pour distribuer les masques.

## Développer

### Prérequis

- Java 11
- Gradle
- node.js (version 10 ou supérieure) & NPM

### Installer le projet

```console
git clone https://github.com/lab-mi/deplacement-covid-19.git
cd deplacement-covid-19
npm i
npm build-dev
```

La commande `./gradlew fullStackBuild` lance :

- Les tests JS et Java
- Les étapes de build JS et Java

### Générer le code de production

```console
./gradlew fullStackBuild
```

Cette commande lance :

- Les tests JS et Java
- Les étapes de build JS et Java

## Configuration et Hébergement

- L'application peut être hébergée sur n'importe quel fournisseur.
- Le paragraphe suivant indique la procédure à suivre pour un hébergement sur Scalingo

### Exemple : configuration pour Scalingo

Les variables d'environnements suivantes doivent être renseignées dans l'interface
Scalingo :

- `BUILDPACK_URL` Doit être valorisé à `https://github.com/Scalingo/multi-buildpack.git`
  afin de pouvoir utiliser plusieurs buildpack (cf le fichier `.buildpacks`)
- `DATABASE_URL` Le hostname pour se connecter à Postgres
- `DATABASE_PASSWORD` Le mot de passe pour se connecter à Postgres
- `DATABASE_USERNAME` Le nom d'utilisateur pour se connecter à Postgres
- `ELK_URL` L'url utilisé pour déversé les logs dans ELK
- `HASH_PEPPER_IDENTITE` La valeur du poivre à utiliser pour hasher les identités
- `NPM_CONFIG_PRODUCTION` Activer la configuration de production pour npm
- `SCALINGO_POSTGRESQL_URL` url ajoutée automatiquement par l'addon Postgres
- `ACTIVATE_ANALYTICS` le booléen pour activer ou désactiver les analytics
- `DB_POOL_SIZE` la taille de pool de connexion à la base (15 en dev)

Commencez par exporter une variable ciblant l'environnement souhaité, récupérer
le pepper utilisé par les applications dans les différents environnements

```bash
export env_name=dev
export HASH_PEPPER_IDENTITE=$HASH_PEPPER_IDENTITE
```

```bash
scalingo --app remettant-${env_name} env-set BUILDPACK_URL=https://github.com/Scalingo/multi-buildpack.git
eval $(scalingo -a remettant-${env_name} env | grep "^SCALINGO_POSTGRESQL_URL=" )
eval $(echo $SCALINGO_POSTGRESQL_URL | sed -r 's/postgres:\/\/((.*):(.*)@){0,1}(.*)/DATABASE_URL=jdbc:postgresql:\/\/\4 \nDATABASE_USERNAME=\2 \nDATABASE_PASSWORD=\3/')
scalingo --app remettant-${env_name} env-set DATABASE_URL=${DATABASE_URL}
scalingo --app remettant-${env_name} env-set DATABASE_PASSWORD=${DATABASE_PASSWORD}
scalingo --app remettant-${env_name} env-set DATABASE_USERNAME=${DATABASE_USERNAME}
eval $(scalingo -a logstash-${env_name} env | grep "^PASSWORD=" )
eval $(scalingo -a logstash-${env_name} env | grep "^USER=" )
LOGSTASH_URL=https://${USER}:${PASSWORD}@logstash-${env_name}.osc-fr1.scalingo.io
scalingo --app remettant-${env_name} env-set ELK_URL=${LOGSTASH_URL}
scalingo --app remettant-${env_name} env-set HASH_PEPPER_IDENTITE=$HASH_PEPPER_IDENTITE
scalingo --app remettant-${env_name} env-set NPM_CONFIG_PRODUCTION=true
scalingo --app remettant-${env_name} env-set DB_POOL_SIZE=15

```

### Forcer le https

```bash
scalingo --app remettant-${env_name} force-https
```

## Crédits

Le projet open source [jsQR](https://github.com/cozmo/jsQR) a été utilisé pour le développement de ce service, en particulier la lecture d'un QR Code.

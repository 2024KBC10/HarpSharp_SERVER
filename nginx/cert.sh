docker-compose run --rm --entrypoint "\
    certbot certonly \
    -d *.exampledomaim.shop \
    --email example@gmail.com \
    --manual --preferred-challenges dns \
    --server https://acme-v02.api.letsencrypt.org/directory \
    --force-renewal" certbot
echo

echo "### Reloading nginx ..."
docker-compose exec nginx nginx -s reload
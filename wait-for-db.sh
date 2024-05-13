#!/bin/sh

set -e

host_and_port="$1"
hostname="${host_and_port%:*}"
port="${host_and_port#*:}"
shift
cmd="$@"

until nc -z "$hostname" "$port"; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

>&2 echo "Postgres is up - executing command"
exec $cmd
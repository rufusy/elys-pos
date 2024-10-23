#!/bin/bash

# Start PostgreSQL in the background
#echo "Starting PostgreSQL..."
#pg_ctl start -D "$PGDATA"

# Wait for PostgresSQL to be up and ready
echo "Waiting for PostgresSQL to start..."
until pg_isready -h postgres -p 5432 -U "$POSTGRES_USER"; do
    sleep 2
done

# Delete the flag file if it exists (in case of a restart)
FLAG_FILE="/tmp/db_initialized"
if [ -f "$FLAG_FILE" ]; then
    rm "$FLAG_FILE"
    echo "Removed existing flag file."
fi

echo "PostgresSQL is up. Running startup scripts..."

$SCRIPT_PATH="/tmp/postgres-init.sql"
# Run the custom SQL script to create databases
if [ -f "$SCRIPT_PATH" ]; then
    psql -U "$POSTGRES_USER" -f "$SCRIPT_PATH"
    if [ $? -eq 0 ]; then
        echo "Database creation script executed successfully."
        # Create a flag file to indicate that initialization is complete
        touch "$FLAG_FILE"
        echo "Flag file created."
    else
        echo "Error executing the database creation script."
        exit 1
    fi
else
    echo "SQL script not found at $SCRIPT_PATH. Exiting."
    exit 1
fi

echo "Startup scripts completed."

# Keep the container running PostgresSQL in the foreground
#postgres

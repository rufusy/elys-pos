DO
$$
BEGIN
    -- Check and create the 'inventory' database
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'inventory') THEN
        EXECUTE 'CREATE DATABASE inventory';
    END IF;
END
$$;

DO
$$
BEGIN
    -- Check and create the 'pricing' database
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'pricing') THEN
        EXECUTE 'CREATE DATABASE pricing';
    END IF;
END
$$;



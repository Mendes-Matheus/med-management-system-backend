ALTER TABLE usuarios
    DROP CONSTRAINT IF EXISTS usuarios_role_check;

ALTER TABLE usuarios
    ADD CONSTRAINT usuarios_role_check
        CHECK (role IN ('ADMINISTRADOR', 'ASSISTENTE_ADMINISTRATIVO'));


ALTER TABLE usuarios
    ALTER COLUMN password TYPE VARCHAR(200),
        ALTER COLUMN role TYPE VARCHAR(50);


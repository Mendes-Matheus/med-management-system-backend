CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT,
    nome VARCHAR(200) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(10),
    complemento VARCHAR(20),
    cidade VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT usuarios_cpf_unique UNIQUE (cpf),
    CONSTRAINT usuarios_username_unique UNIQUE (username),
    CONSTRAINT usuarios_email_unique UNIQUE (email)
);

CREATE INDEX idx_usuarios_nome ON usuarios(nome);
CREATE INDEX idx_usuarios_cidade ON usuarios(cidade);
CREATE INDEX idx_usuarios_telefone ON usuarios(telefone);
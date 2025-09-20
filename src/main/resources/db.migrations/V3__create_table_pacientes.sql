CREATE TABLE pacientes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT,
    nome VARCHAR(200) NOT NULL,
    sexo VARCHAR(20) NOT NULL,
    data_nascimento DATE NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    rg VARCHAR(20) NOT NULL,
    cns VARCHAR(20) NOT NULL,
    nome_pai VARCHAR(200) NOT NULL,
    nome_mae VARCHAR(200),
    telefone VARCHAR(20) NOT NULL,
    telefone_secundario VARCHAR(20),
    email VARCHAR(100),
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    observacoes VARCHAR(1000),
    CONSTRAINT pacientes_cpf_unique UNIQUE (cpf),
    CONSTRAINT pacientes_cns_unique UNIQUE (cns)
);

CREATE INDEX idx_pacientes_nome ON pacientes(nome);
CREATE INDEX idx_pacientes_telefone ON pacientes(telefone);
CREATE INDEX idx_pacientes_email ON pacientes(email);
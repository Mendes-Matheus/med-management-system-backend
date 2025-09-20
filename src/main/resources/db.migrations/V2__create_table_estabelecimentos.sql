CREATE TABLE estabelecimentos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT,
    nome_estabelecimento VARCHAR(200) NOT NULL,
    observacoes VARCHAR(1000)
);

CREATE INDEX idx_estabelecimentos_nome ON estabelecimentos(nome_estabelecimento);
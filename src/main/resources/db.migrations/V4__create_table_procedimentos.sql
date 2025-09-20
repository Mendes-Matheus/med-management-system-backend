CREATE TABLE procedimentos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT,
    nome_procedimento VARCHAR(100) NOT NULL,
    tipo_procedimento VARCHAR(100) NOT NULL,
    estabelecimento_id UUID NOT NULL,
    observacoes VARCHAR(1000),
    orientacoes VARCHAR(1000),
    CONSTRAINT fk_procedimentos_estabelecimento FOREIGN KEY (estabelecimento_id) REFERENCES estabelecimentos(id)
);

CREATE INDEX idx_procedimentos_nome ON procedimentos(nome_procedimento);
CREATE INDEX idx_procedimentos_tipo ON procedimentos(tipo_procedimento);
CREATE INDEX idx_procedimentos_estabelecimento ON procedimentos(estabelecimento_id);
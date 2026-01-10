CREATE TABLE procedimento_estabelecimento (
    procedimento_id UUID NOT NULL,
    estabelecimento_id UUID NOT NULL,
    PRIMARY KEY (procedimento_id, estabelecimento_id),
    CONSTRAINT fk_proc_est_procedimento FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id),
    CONSTRAINT fk_proc_est_estabelecimento FOREIGN KEY (estabelecimento_id) REFERENCES estabelecimentos(id)
);

CREATE INDEX idx_proc_est_procedimento ON procedimento_estabelecimento(procedimento_id);
CREATE INDEX idx_proc_est_estabelecimento ON procedimento_estabelecimento(estabelecimento_id);



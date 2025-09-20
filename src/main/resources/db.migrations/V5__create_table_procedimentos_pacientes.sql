CREATE TABLE procedimentos_paciente (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT,
    status VARCHAR(100),
    data_solicitacao DATE,
    data_agendamento DATE,
    observacoes VARCHAR(1000),
    paciente_id UUID,
    procedimento_id UUID,
    CONSTRAINT fk_procedimentos_paciente_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_procedimentos_paciente_procedimento FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id)
);

CREATE INDEX idx_procedimentos_paciente_status ON procedimentos_paciente(status);
CREATE INDEX idx_procedimentos_paciente_paciente ON procedimentos_paciente(paciente_id);
CREATE INDEX idx_procedimentos_paciente_procedimento ON procedimentos_paciente(procedimento_id);
CREATE INDEX idx_procedimentos_paciente_data_agendamento ON procedimentos_paciente(data_agendamento);
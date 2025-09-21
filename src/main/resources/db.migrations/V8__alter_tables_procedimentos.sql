ALTER TABLE procedimentos
    DROP CONSTRAINT IF EXISTS procedimentos_tipo_procedimento_check;

ALTER TABLE procedimentos ADD CONSTRAINT procedimentos_tipo_procedimento_check
    CHECK (tipo_procedimento IN ('CONSULTA', 'EXAME', 'CIRURGIA'));
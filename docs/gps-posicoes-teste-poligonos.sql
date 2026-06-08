-- Ajusta a API de telemetria para retornar pontos dentro dos poligonos cadastrados no ORDENA SPACE.
-- Banco alvo: tabela GPS_POSICOES da API externa.
-- Estrategia: usar um ponto interno (centroide medio) de cada poligono cadastrado.

-- 80001 -> Barra da Tijuca
UPDATE GPS_POSICOES
SET LATITUDE = -23.001275,
    LONGITUDE = -43.341550,
    CAPTURADO_EM = SYSTIMESTAMP,
    OBSERVACAO = 'dentro do poligono Barra da Tijuca'
WHERE TABLET_SATELITAL = '80001';

-- 80002 -> Botafogo
UPDATE GPS_POSICOES
SET LATITUDE = -22.953050,
    LONGITUDE = -43.192525,
    CAPTURADO_EM = SYSTIMESTAMP,
    OBSERVACAO = 'dentro do poligono Botafogo'
WHERE TABLET_SATELITAL = '80002';

-- 80003 -> Centro
UPDATE GPS_POSICOES
SET LATITUDE = -22.903075,
    LONGITUDE = -43.188225,
    CAPTURADO_EM = SYSTIMESTAMP,
    OBSERVACAO = 'dentro do poligono Centro'
WHERE TABLET_SATELITAL = '80003';

-- 80004 -> Copacabana
UPDATE GPS_POSICOES
SET LATITUDE = -22.979075,
    LONGITUDE = -43.199900,
    CAPTURADO_EM = SYSTIMESTAMP,
    OBSERVACAO = 'dentro do poligono Copacabana'
WHERE TABLET_SATELITAL = '80004';

-- 80005 -> Flamengo
UPDATE GPS_POSICOES
SET LATITUDE = -22.934075,
    LONGITUDE = -43.182775,
    CAPTURADO_EM = SYSTIMESTAMP,
    OBSERVACAO = 'dentro do poligono Flamengo'
WHERE TABLET_SATELITAL = '80005';

-- 80006 -> Ipanema
UPDATE GPS_POSICOES
SET LATITUDE = -22.982750,
    LONGITUDE = -43.219825,
    CAPTURADO_EM = SYSTIMESTAMP,
    OBSERVACAO = 'dentro do poligono Ipanema'
WHERE TABLET_SATELITAL = '80006';

COMMIT;

-- Coordenadas extras caso voce queira criar mais tablets de teste:
-- Leblon  -> -22.989450, -43.232675
-- Urca    -> -22.954225, -43.175625

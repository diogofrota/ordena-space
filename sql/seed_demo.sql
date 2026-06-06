-- ORDENA SPACE
-- Seed demonstrativo para Oracle Database

INSERT INTO usuarios (email, senha_hash, perfil)
VALUES ('policial@policial.com', 'fa13a3cbc24031203b00a4fb2ca5b32eb9d5e46a48ac8d9924db55c9c063c944', 'ADMIN');

INSERT INTO setores (nome) VALUES ('Copacabana');
INSERT INTO setores (nome) VALUES ('Ipanema');
INSERT INTO setores (nome) VALUES ('Barra da Tijuca');
INSERT INTO setores (nome) VALUES ('Leblon');
INSERT INTO setores (nome) VALUES ('Centro');
INSERT INTO setores (nome) VALUES ('Urca');
INSERT INTO setores (nome) VALUES ('Botafogo');
INSERT INTO setores (nome) VALUES ('Flamengo');

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -22.988600, -43.205700 FROM setores WHERE nome = 'Copacabana';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -22.982900, -43.189800 FROM setores WHERE nome = 'Copacabana';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.970700, -43.193700 FROM setores WHERE nome = 'Copacabana';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -22.974100, -43.210400 FROM setores WHERE nome = 'Copacabana';

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -22.992700, -43.226700 FROM setores WHERE nome = 'Ipanema';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -22.983500, -43.206900 FROM setores WHERE nome = 'Ipanema';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.973900, -43.213100 FROM setores WHERE nome = 'Ipanema';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -22.980900, -43.232600 FROM setores WHERE nome = 'Ipanema';

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -23.007900, -43.347300 FROM setores WHERE nome = 'Barra da Tijuca';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -23.002300, -43.323500 FROM setores WHERE nome = 'Barra da Tijuca';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.994600, -43.336300 FROM setores WHERE nome = 'Barra da Tijuca';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -23.000300, -43.359100 FROM setores WHERE nome = 'Barra da Tijuca';

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -22.999300, -43.241600 FROM setores WHERE nome = 'Leblon';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -22.989800, -43.218400 FROM setores WHERE nome = 'Leblon';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.981600, -43.224500 FROM setores WHERE nome = 'Leblon';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -22.987100, -43.246200 FROM setores WHERE nome = 'Leblon';

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -22.914700, -43.192200 FROM setores WHERE nome = 'Centro';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -22.903900, -43.176300 FROM setores WHERE nome = 'Centro';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.893500, -43.183500 FROM setores WHERE nome = 'Centro';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -22.900200, -43.200900 FROM setores WHERE nome = 'Centro';

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -22.962400, -43.181700 FROM setores WHERE nome = 'Urca';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -22.955800, -43.164900 FROM setores WHERE nome = 'Urca';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.948200, -43.169700 FROM setores WHERE nome = 'Urca';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -22.950500, -43.186200 FROM setores WHERE nome = 'Urca';

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -22.962500, -43.197700 FROM setores WHERE nome = 'Botafogo';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -22.954400, -43.180300 FROM setores WHERE nome = 'Botafogo';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.944700, -43.187600 FROM setores WHERE nome = 'Botafogo';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -22.950600, -43.204500 FROM setores WHERE nome = 'Botafogo';

INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 1, -22.944900, -43.186700 FROM setores WHERE nome = 'Flamengo';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 2, -22.935300, -43.171200 FROM setores WHERE nome = 'Flamengo';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 3, -22.924600, -43.178400 FROM setores WHERE nome = 'Flamengo';
INSERT INTO setor_pontos_gps (setor_id, ordem_ponto, latitude, longitude)
SELECT id, 4, -22.931500, -43.194800 FROM setores WHERE nome = 'Flamengo';

INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70001', 'Soldado', 'Falcao');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70002', 'Soldado', 'Sentinela');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70003', 'Cabo', 'Aguia');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70004', 'Cabo', 'Titan');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70005', 'Sargento', 'Bravo');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70006', 'Soldado', 'Guardian');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70007', 'Cabo', 'Fenix');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70008', 'Sargento', 'Orion');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70009', 'Soldado', 'Lince');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70010', 'Cabo', 'Vulcano');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70011', 'Sargento', 'Atlas');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70012', 'Soldado', 'Delta');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70013', 'Cabo', 'Trovao');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70014', 'Soldado', 'Ranger');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70015', 'Sargento', 'Cometa');
INSERT INTO policiais (rg, graduacao, nome_guerra) VALUES ('70016', 'Soldado', 'Escudo');

INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1001', 'RIO5A01', '80001');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1002', 'RIO5A02', '80002');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1003', 'RIO5A03', '80003');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1004', 'RIO5A04', '80004');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1005', 'RIO5A05', '80005');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1006', 'RIO5A06', '80006');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1007', 'RIO5A07', '80007');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1008', 'RIO5A08', '80008');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1009', 'RIO5A09', '80009');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1010', 'RIO5A10', '80010');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1011', 'RIO5A11', '80011');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1012', 'RIO5A12', '80012');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1013', 'RIO5A13', '80013');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1014', 'RIO5A14', '80014');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1015', 'RIO5A15', '80015');
INSERT INTO viaturas (numero, placa, tablet_satelital) VALUES ('54-1016', 'RIO5A16', '80016');

COMMIT;

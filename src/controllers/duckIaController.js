// src/controllers/duckIaController.js
const { GoogleGenAI } = require("@google/genai");
const db = require("../database/config");

const chatIA = new GoogleGenAI({ apiKey: process.env.GEMINI_KEY });

// ─── SYSTEM PROMPT ────────────────────────────────────────────────────────────
const SYSTEM_PROMPT = `
You are Duck IA, the intelligent assistant embedded in the DataSafeEduc system.

ABSOLUTE RULES — apply to every single response, no exceptions, no matter what the user asks:
1. ALWAYS respond in Brazilian Portuguese (pt-BR). Never use any other language.
2. MAXIMUM 3 paragraphs. Hard limit. Even for step-by-step guides, fit everything into 3 paragraphs.
3. Each paragraph must have at most 3 sentences. Be concise.
4. ONLY answer questions about the DataSafeEduc system. If the question is off-topic, respond exactly: "Só consigo responder dúvidas sobre o DataSafeEduc."
5. Never use markdown formatting such as **, ##, or bullet symbols (-, *). Write plain text only.
6. Never repeat or paraphrase the user's question. Go straight to the answer.
7. Never invent information. If you don't know, say: "Não tenho essa informação disponível."
8. Do not add conclusions, summaries, or closing sentences like "Espero ter ajudado."

=== SYSTEM OVERVIEW ===
DataSafeEduc is an educational analytics platform that measures and monitors student performance
on the ENEM (Brazilian national high school exam), focused on municipalities in the State of São Paulo.
It serves three user roles: Gestor, Coordenador, and Professor.

=== USER ROLES & PERMISSIONS ===
Gestor (Admin): full system access; can create, edit, and delete any user.
Coordenador: manages professors within their municipality; views all dashboards.
Professor: read-only access to dashboards and student results for their municipality.

=== CORE FEATURES ===
Performance Analysis by Theme: charts showing correct answer rates per knowledge area.
Performance Monitoring by Skill: tracks student evolution by BNCC skill code over time.
Performance List by Question: detailed table with per-question results and metadata.
Simulados (Mock Exams): create and manage practice exams using real ENEM questions, filtered by area, skill, difficulty, and year.
Teacher Management: register and control professor access.
Coordinator Management: register and control coordinator access.
Duck IA: this AI assistant, available to answer questions about the system.

=== DATABASE SCHEMA (main tables) ===
usuario: idUsuario, nome, senha, data_criacao, nivel_acesso (FK), municipio (FK)
nivel_acesso: idnivel_acesso, nome (gestor | coordenador | professor)
municipio: idMunicipio, nome, estado
questao: codigoItem, ano_exame, habilidade (FK), area_conhecimento (FK), dificuldade (FK)
habilidade: numero (e.g. H1, H2), descricao
area_conhecimento: CN (Natural Sciences), CH (Human Sciences), LC (Languages), MT (Mathematics)
dificuldade: nivel (fácil | médio | difícil), parametro_a, parametro_b, parametro_c
simulado: idSimulado, nomeSimulado, quantidadeQuestoes, usuario_responsavel (FK)
questao_has_simulado: N:N junction table between questao and simulado

=== ITEM RESPONSE THEORY (IRT / TRI) ===
Difficulty levels use the 3-parameter logistic model (3PL):
parameter_a: discrimination — how well the item differentiates between ability levels.
parameter_b: difficulty — proficiency level at which a student has 50% chance of answering correctly.
parameter_c: guessing — minimum probability of a correct answer by chance.

=== HOW-TO GUIDES ===
REGISTERING A NEW USER: Navigate to Teacher or Coordinator Management in the sidebar. Click Adicionar, fill in name, password and municipality, then save. Access level is assigned automatically based on the screen.

CREATING A SIMULADO: Navigate to Simulados in the sidebar. Click Novo Simulado, set name and number of questions, apply filters (area, skill, difficulty, year), then confirm.

RESETTING A USER PASSWORD: The Gestor edits the target user's record directly in the management screen.

=== NAVIGATION ===
Análise e Desempenho por Tema → /analise-desempenho
Monitoramento de Desempenho por Habilidade → /dash-monitoramento
Lista de Desempenho por Questão → /lista-desempenho
Simulados → /simulados
Gerenciamento de Professores → /gerenciamento-professores
Gerenciamento de Coordenadores → /gerenciamento-coordenadores
Duck IA → /duck-ia
`;

// ─── FUNÇÃO PRINCIPAL ─────────────────────────────────────────────────────────
async function responder(req, res) {
    const { pergunta, historico } = req.body;

    if (!pergunta || pergunta.trim() === "") {
        return res.status(400).json({ error: "Pergunta não pode ser vazia." });
    }

    try {
        const contextoBanco = await buscarContextoBanco(pergunta);

        // Limita o histórico às últimas 6 mensagens para não acumular padrões longos
        const historicoRecente = Array.isArray(historico) ? historico.slice(-6) : [];
        const mensagens = [];

        for (const item of historicoRecente) {
            mensagens.push({
                role: item.papel === "usuario" ? "user" : "model",
                parts: [{ text: item.texto }]
            });
        }

        // Reforça as regras diretamente na mensagem do usuário — o Gemini
        // sempre lê a última mensagem com mais atenção do que o systemInstruction
        const instrucaoFinal = `[REGRAS OBRIGATÓRIAS: responda em pt-BR, máximo 3 parágrafos curtos, sem markdown, somente sobre o DataSafeEduc]\n\nPergunta: ${pergunta}`;

        const perguntaFinal = contextoBanco
            ? `${instrucaoFinal}\n\n[Dados do sistema consultados agora]:\n${contextoBanco}`
            : instrucaoFinal;

        mensagens.push({
            role: "user",
            parts: [{ text: perguntaFinal }]
        });

        const modeloIA = chatIA.models.generateContent({
            model: "gemini-2.5-flash",
            systemInstruction: SYSTEM_PROMPT,
            contents: mensagens,
            generationConfig: {
                temperature: 0,
                maxOutputTokens: 400, // reduzido — força respostas curtas
            }
        });

        const resultado = await modeloIA;
        let resposta = resultado.text;

        // Garante no máximo 3 parágrafos mesmo que o modelo ignore o limite
        resposta = limitarParagrafos(resposta, 3);

        console.log(`[Duck IA] Pergunta: "${pergunta}"`);
        console.log(`[Duck IA] Tokens:`, resultado.usageMetadata);

        return res.json({ resultado: resposta });

    } catch (error) {
        console.error("[Duck IA] Erro:", error);
        return res.status(500).json({ error: "Erro interno ao processar sua pergunta." });
    }
}

// ─── LIMITE DE PARÁGRAFOS ─────────────────────────────────────────────────────
// Última linha de defesa: corta a resposta no 3º parágrafo independente do modelo
function limitarParagrafos(texto, max) {
    const paragrafos = texto
        .split(/\n\s*\n/)           // divide por linha em branco
        .map(p => p.trim())
        .filter(p => p.length > 0);

    return paragrafos.slice(0, max).join("\n\n");
}

// ─── BUSCA CONTEXTO NO BANCO ──────────────────────────────────────────────────
async function buscarContextoBanco(pergunta) {
    const p = pergunta.toLowerCase();
    const partes = [];

    try {
        if (p.includes("habilidade") || p.includes("h1") || p.includes("h2")) {
            const rows = await db.executar(
                "SELECT numero, descricao FROM habilidade LIMIT 20"
            ).catch(() => null);
            if (rows && rows.length > 0) {
                partes.push("Habilidades cadastradas:\n" +
                    rows.map(r => `${r.numero}: ${r.descricao}`).join("\n"));
            }
        }

        if (p.includes("simulado")) {
            const rows = await db.executar(
                "SELECT nomeSimulado, quantidadeQuestoes FROM simulado LIMIT 10"
            ).catch(() => null);
            if (rows && rows.length > 0) {
                partes.push("Simulados no sistema:\n" +
                    rows.map(r => `"${r.nomeSimulado}" — ${r.quantidadeQuestoes} questões`).join("\n"));
            }
        }

        if (p.includes("professor") || p.includes("coordenador") || p.includes("usuário") || p.includes("usuario")) {
            const rows = await db.executar(`
                SELECT u.nome, na.nome AS cargo, m.nome AS municipio
                FROM usuario u
                LEFT JOIN nivel_acesso na ON u.nivel_acesso_idnivel_acesso = na.idnivel_acesso
                LEFT JOIN municipio m ON u.municipio_idMunicipio = m.idMunicipio
                LIMIT 15
            `).catch(() => null);
            if (rows && rows.length > 0) {
                partes.push("Usuários cadastrados:\n" +
                    rows.map(r => `${r.nome} (${r.cargo || "sem cargo"} — ${r.municipio || "sem município"})`).join("\n"));
            }
        }

        if (p.includes("desempenho") || p.includes("nota") || p.includes("resultado")) {
            const rows = await db.executar(`
                SELECT municipio,
                       AVG(nota_mt) as media_mt,
                       AVG(nota_cn) as media_cn,
                       AVG(nota_ch) as media_ch,
                       AVG(nota_lc) as media_lc
                FROM nota_municipal
                GROUP BY municipio
                ORDER BY media_mt DESC
                LIMIT 10
            `).catch(() => null);
            if (rows && rows.length > 0) {
                partes.push("Médias ENEM por município:\n" +
                    rows.map(r =>
                        `${r.municipio}: MT=${Number(r.media_mt).toFixed(1)}, ` +
                        `CN=${Number(r.media_cn).toFixed(1)}, ` +
                        `CH=${Number(r.media_ch).toFixed(1)}, ` +
                        `LC=${Number(r.media_lc).toFixed(1)}`
                    ).join("\n"));
            }
        }

    } catch (err) {
        console.warn("[Duck IA] Aviso banco:", err.message);
    }

    return partes.join("\n\n");
}

module.exports = { responder };
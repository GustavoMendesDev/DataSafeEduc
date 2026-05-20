// src/controllers/duckIaController.js
const { GoogleGenAI } = require("@google/genai");
const db = require("../database/config");

const chatIA = new GoogleGenAI({ apiKey: process.env.GEMINI_KEY });

// ─── SYSTEM PROMPT ────────────────────────────────────────────────────────────
// Aqui você alimenta a Duck IA com todo o conhecimento do sistema.
// Edite esta string para adicionar mais contexto sobre seu site/dados.
const SYSTEM_PROMPT = `
You are Duck IA, the intelligent assistant of the DataSafeEduc system.
You MUST always respond in Brazilian Portuguese (pt-BR), regardless of the language used to ask the question.
Be clear, objective, and limit your answers to 3 paragraphs maximum.
Never fabricate information. If you do not have the data, explicitly say so.
Do not answer questions unrelated to the DataSafeEduc system or education topics.

=== SYSTEM OVERVIEW ===
DataSafeEduc is an educational analytics platform that measures and monitors student performance
on the ENEM (Brazilian national high school exam), focused on municipalities in the State of São Paulo.
It serves three user roles with different permission levels: Gestor, Coordenador, and Professor.

=== USER ROLES & PERMISSIONS ===
- Gestor (Admin): full system access; can create, edit, and delete any user.
- Coordenador: manages professors within their municipality; views all dashboards.
- Professor: read-only access to dashboards and student results for their municipality.

=== CORE FEATURES ===
- Performance Analysis by Theme: charts showing correct answer rates per knowledge area.
- Performance Monitoring by Skill: tracks student evolution by BNCC skill code over time.
- Performance List by Question: detailed table with per-question results and metadata.
- Simulados (Mock Exams): create and manage practice exams using real ENEM questions, filtered by area, skill, difficulty, and year.
- Teacher Management: register and control professor access.
- Coordinator Management: register and control coordinator access.
- Duck IA: this AI assistant, available to answer questions about the system.

=== DATABASE SCHEMA (main tables) ===
- usuario: idUsuario, nome, senha, data_criacao, nivel_acesso (FK), municipio (FK)
- nivel_acesso: idnivel_acesso, nome (gestor | coordenador | professor)
- municipio: idMunicipio, nome, estado
- questao: codigoItem, ano_exame, habilidade (FK), area_conhecimento (FK), dificuldade (FK)
- habilidade: numero (e.g. H1, H2), descricao
- area_conhecimento: CN (Natural Sciences), CH (Human Sciences), LC (Languages), MT (Mathematics)
- dificuldade: nivel (fácil | médio | difícil), parametro_a, parametro_b, parametro_c
- simulado: idSimulado, nomeSimulado, quantidadeQuestoes, usuario_responsavel (FK)
- questao_has_simulado: N:N junction table between questao and simulado

=== ITEM RESPONSE THEORY (IRT / TRI) ===
Difficulty levels are based on IRT three-parameter logistic model (3PL):
- parameter_a: discrimination — how well the item differentiates between ability levels.
- parameter_b: difficulty — the proficiency level at which a student has a 50% chance of answering correctly.
- parameter_c: guessing — the minimum probability of a correct answer by chance.

=== HOW-TO GUIDES ===

REGISTERING A NEW USER:
1. Navigate to Teacher Management or Coordinator Management in the sidebar.
2. Click "Adicionar".
3. Fill in the name, password, and municipality.
4. The access level is automatically assigned based on the current management screen.
5. Click save — the user can log in immediately.

CREATING A SIMULADO (Mock Exam):
1. Navigate to "Simulados" in the sidebar.
2. Click "Novo Simulado".
3. Set the name and number of questions.
4. Apply filters: knowledge area, skill code, difficulty level, and exam year.
5. Confirm — the system automatically selects matching questions.

RESETTING A USER PASSWORD:
Password resets are performed by the Gestor through the user management screen,
by editing the target user's registration record directly.

=== BEHAVIORAL RULES ===
- Always respond in Brazilian Portuguese (pt-BR), even if the question is in another language.
- Keep responses under 3 paragraphs unless a step-by-step guide is explicitly requested.
- When listing steps, use numbered format for clarity.
- If the user asks about data not available in the context provided, respond with:
  "Não tenho essa informação disponível no momento."
- Do not speculate about features, data, or behaviors not described above.
`;

// ─── FUNÇÃO PRINCIPAL ─────────────────────────────────────────────────────────
async function responder(req, res) {
    const { pergunta, historico } = req.body;

    if (!pergunta || pergunta.trim() === "") {
        return res.status(400).json({ error: "Pergunta não pode ser vazia." });
    }

    try {
        // Monta o contexto de dados do banco para enriquecer a resposta
        const contextoBanco = await buscarContextoBanco(pergunta);

        // Monta o array de mensagens (histórico da conversa)
        const mensagens = [];

        // Injeta histórico anterior (se o frontend enviar)
        if (Array.isArray(historico)) {
            for (const item of historico) {
                mensagens.push({
                    role: item.papel === "usuario" ? "user" : "model",
                    parts: [{ text: item.texto }]
                });
            }
        }

        // Pergunta atual com contexto do banco injetado
        const perguntaEnriquecida = contextoBanco
            ? `${pergunta}\n\n[Dados relevantes do sistema]:\n${contextoBanco}`
            : pergunta;

        mensagens.push({
            role: "user",
            parts: [{ text: perguntaEnriquecida }]
        });

        // Chama o Gemini com temperature: 0 para respostas determinísticas
        const modeloIA = chatIA.models.generateContent({
            model: "gemini-2.5-flash",
            systemInstruction: SYSTEM_PROMPT,
            contents: mensagens,
            generationConfig: {
                temperature: 0,         // respostas determinísticas (sem criatividade)
                maxOutputTokens: 1024,
            }
        });

        const resultado = await modeloIA;
        const resposta = resultado.text;

        console.log(`[Duck IA] Pergunta: "${pergunta}"`);
        console.log(`[Duck IA] Tokens:`, resultado.usageMetadata);

        return res.json({ resultado: resposta });

    } catch (error) {
        console.error("[Duck IA] Erro:", error);
        return res.status(500).json({ error: "Erro interno ao processar sua pergunta." });
    }
}

// ─── BUSCA CONTEXTO NO BANCO ──────────────────────────────────────────────────
// Detecta palavras-chave na pergunta e busca dados reais do banco para injetar
// no prompt. Expanda esta função conforme suas tabelas crescem.
async function buscarContextoBanco(pergunta) {
    const p = pergunta.toLowerCase();
    const partes = [];

    try {
        // Se pergunta for sobre habilidades, busca as habilidades do banco
        if (p.includes("habilidade") || p.includes("h1") || p.includes("h2")) {
            const habilidades = await db.executar(
                "SELECT numero, descricao FROM habilidade LIMIT 20"
            );
            if (habilidades && habilidades.length > 0) {
                const lista = habilidades.map(h => `${h.numero}: ${h.descricao}`).join("\n");
                partes.push(`Habilidades cadastradas:\n${lista}`);
            }
        }

        // Se pergunta for sobre simulados
        if (p.includes("simulado")) {
            const simulados = await db.executar(
                "SELECT nomeSimulado, quantidadeQuestoes FROM simulado LIMIT 10"
            );
            if (simulados && simulados.length > 0) {
                const lista = simulados.map(s => `"${s.nomeSimulado}" (${s.quantidadeQuestoes} questões)`).join("\n");
                partes.push(`Simulados existentes no sistema:\n${lista}`);
            }
        }

        // Se pergunta for sobre usuários ou professores
        if (p.includes("professor") || p.includes("usuário") || p.includes("usuario") || p.includes("coordenador")) {
            const usuarios = await db.executar(
                `SELECT u.nome, na.nome AS cargo, m.nome AS municipio
                 FROM usuario u
                 LEFT JOIN nivel_acesso na ON u.nivel_acesso_idnivel_acesso = na.idnivel_acesso
                 LEFT JOIN municipio m ON u.municipio_idMunicipio = m.idMunicipio
                 LIMIT 15`
            );
            if (usuarios && usuarios.length > 0) {
                const lista = usuarios.map(u => `${u.nome} (${u.cargo || "sem cargo"} - ${u.municipio || "sem município"})`).join("\n");
                partes.push(`Usuários cadastrados no sistema:\n${lista}`);
            }
        }

        // Se pergunta for sobre desempenho ou notas
        if (p.includes("desempenho") || p.includes("nota") || p.includes("resultado")) {
            const notas = await db.executar(
                `SELECT municipio, AVG(nota_mt) as media_mt, AVG(nota_cn) as media_cn,
                        AVG(nota_ch) as media_ch, AVG(nota_lc) as media_lc
                 FROM nota_municipal
                 GROUP BY municipio
                 ORDER BY media_mt DESC
                 LIMIT 10`
            ).catch(() => null); // Silencioso se tabela não existir ainda

            if (notas && notas.length > 0) {
                const lista = notas.map(n =>
                    `${n.municipio}: MT=${Number(n.media_mt).toFixed(1)}, CN=${Number(n.media_cn).toFixed(1)}, CH=${Number(n.media_ch).toFixed(1)}, LC=${Number(n.media_lc).toFixed(1)}`
                ).join("\n");
                partes.push(`Médias por município (ENEM):\n${lista}`);
            }
        }

    } catch (err) {
        // Não bloqueia se o banco falhar — a IA responde só com o system prompt
        console.warn("[Duck IA] Aviso ao buscar contexto do banco:", err.message);
    }

    return partes.join("\n\n");
}

module.exports = { responder };
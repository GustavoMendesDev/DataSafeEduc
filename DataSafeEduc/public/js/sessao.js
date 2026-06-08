// sessão
// js/sessao.js

// 1. Função que carrega os dados e controla o nível de acesso
function carregarDadosSessao() {
  const nomeLogado = sessionStorage.NOME_USUARIO;
  const cargoLogado = sessionStorage.CARGO_USUARIO; // Ex: 'Administrador', 'Professor', 'Coordenador'

  const elementoNome = document.querySelector('.nome-perfil');
  const elementoCargo = document.querySelector('.cargo-perfil');

  // Injeta os textos na navbar se existirem
  if (nomeLogado && elementoNome) {
    elementoNome.textContent = nomeLogado;
  }
  
  if (cargoLogado && elementoCargo) {
    elementoCargo.textContent = cargoLogado;
    
    // Executa a restrição de telas baseada no cargo
    aplicarRestricoesAcesso(cargoLogado);
  }
}

// 2. Função que esconde as opções da navbar baseado no cargo
function aplicarRestricoesAcesso(cargo) {
  // Captura os links de gerenciamento pelo atributo href do HTML
  const linkProfessores = document.querySelector('a[href="gerenciamento-professores.html"]');
  const linkCoordenadores = document.querySelector('a[href="gerenciamento-coordenadores.html"]');
  const linkAnaliseDesempenho = document.querySelector('a[href="analise-desempenho.html"]');
  const linkDashMonitoramento = document.querySelector('a[href="dash-monitoramento.html"]');

  // Se o usuário NÃO for Administrador, escondemos as telas de gestão
  if (cargo == 'Coordenador') {
    if (linkCoordenadores) linkCoordenadores.style.display = 'none';
  }
  
  if (cargo == 'Professor') {
    if (linkProfessores) linkProfessores.style.display = 'none';
    if (linkCoordenadores) linkCoordenadores.style.display = 'none';
    if (linkAnaliseDesempenho) linkAnaliseDesempenho.style.display = 'none';
    if (linkDashMonitoramento) linkDashMonitoramento.style.display = 'none';
  }
}

// 3. Função que realiza o logout
function realizarLogout() {
  sessionStorage.clear();
  window.location.href = "../index.html"; 
}

// 4. Inicializa tudo quando a página carregar
document.addEventListener("DOMContentLoaded", () => {
  carregarDadosSessao();

  const btnSair = document.getElementById("btnSair");
  if (btnSair) {
    btnSair.addEventListener("click", realizarLogout);
  }
});
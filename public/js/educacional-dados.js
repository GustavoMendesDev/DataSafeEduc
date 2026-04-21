// Dados mockados para o Painel de Desempenho Educacional

const educacionalData = {
  // Dados para "Recorrência por habilidade" - Stacked Bar Chart
  recorrenciaPorHabilidade: {
    labels: ['2019', '2020', '2021', '2022', '2023'],
    datasets: [
      {
        label: 'Fácil',
        data: [65, 78, 54, 82, 91],
        backgroundColor: '#ff6b6b',
        borderWidth: 0,
      },
      {
        label: 'Médio',
        data: [89, 102, 120, 115, 98],
        backgroundColor: '#4ecdc4',
        borderWidth: 0,
      },
      {
        label: 'Difícil',
        data: [156, 134, 198, 167, 145],
        backgroundColor: '#1a2a5e',
        borderWidth: 0,
      },
      {
        label: 'Muito Difícil',
        data: [78, 92, 71, 103, 88],
        backgroundColor: '#ffd93d',
        borderWidth: 0,
      },
    ],
  },

  // Dados para Radar Chart
  radarChart: {
    labels: ['Pessoal Familiar', 'Pessoal Trabalho', 'AMAVI Trabalho', 'AMAVI Familiar'],
    datasets: [
      {
        label: 'Forte',
        data: [65, 75, 70, 80],
        borderColor: '#223488',
        backgroundColor: 'rgba(34, 52, 136, 0.1)',
        borderWidth: 2,
        pointBackgroundColor: '#223488',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 5,
      },
      {
        label: 'Fraca',
        data: [45, 55, 50, 60],
        borderColor: '#ff9800',
        backgroundColor: 'rgba(255, 152, 0, 0.1)',
        borderWidth: 2,
        pointBackgroundColor: '#ff9800',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 5,
      },
    ],
  },

  // Dados para "Habilidades mais frequentes"
  habilidadesFrequentes: {
    labels: ['H23', 'H18', 'H42', 'H15', 'H19', 'H04'],
    datasets: [
      {
        label: 'Série 1',
        data: [156, 134, 198, 167, 145, 189],
        backgroundColor: '#1a2a5e',
        borderWidth: 0,
      },
      {
        label: 'Série 2',
        data: [112, 98, 145, 123, 108, 156],
        backgroundColor: '#d0d0d0',
        borderWidth: 0,
      },
    ],
  },
};

// Painel de Desempenho Educacional - Chart Initialization

// Chart instances storage
const charts = {};

// Initialize all charts when page loads
window.addEventListener('DOMContentLoaded', function () {
  inicializarGraficos();
});

function inicializarGraficos() {
  // Inicializar Radar Chart
  plotarRadarChart();

  // Inicializar Stacked Bar Chart
  plotarStackedBarChart();

  // Inicializar Frequent Skills Bar Chart
  plotarHabilidadesFrequentes();
}

/**
 * Plota o Radar Chart para "Pessoal Amizade"
 */
function plotarRadarChart() {
  const ctx = document.getElementById('radarChart');
  if (!ctx) return;

  const config = {
    type: 'radar',
    data: educacionalData.radarChart,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 12,
            },
            padding: 15,
            usePointStyle: true,
          },
        },
      },
      scales: {
        r: {
          beginAtZero: true,
          max: 100,
          ticks: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 11,
            },
            color: '#64748b',
          },
          grid: {
            color: 'rgba(0, 0, 0, 0.05)',
          },
          pointLabels: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 11,
              weight: '500',
            },
            color: '#1e293b',
          },
        },
      },
    },
  };

  charts.radar = new Chart(ctx, config);
}

/**
 * Plota o Stacked Bar Chart para "Recorrência por habilidade"
 */
function plotarStackedBarChart() {
  const ctx = document.getElementById('stackedBarChart');
  if (!ctx) return;

  const config = {
    type: 'bar',
    data: educacionalData.recorrenciaPorHabilidade,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      indexAxis: 'x',
      scales: {
        x: {
          stacked: true,
          ticks: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 12,
            },
            color: '#1e293b',
          },
          grid: {
            display: false,
          },
        },
        y: {
          stacked: true,
          ticks: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 11,
            },
            color: '#64748b',
          },
          grid: {
            color: 'rgba(0, 0, 0, 0.05)',
          },
        },
      },
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 12,
            },
            padding: 15,
            usePointStyle: true,
          },
        },
        tooltip: {
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          titleFont: {
            family: "'DM Sans', sans-serif",
            size: 12,
          },
          bodyFont: {
            family: "'DM Sans', sans-serif",
            size: 11,
          },
        },
      },
    },
  };

  charts.stackedBar = new Chart(ctx, config);
}

/**
 * Plota o Bar Chart para "Habilidades mais frequentes"
 */
function plotarHabilidadesFrequentes() {
  const ctx = document.getElementById('frequentSkillsChart');
  if (!ctx) return;

  const config = {
    type: 'bar',
    data: educacionalData.habilidadesFrequentes,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        x: {
          ticks: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 12,
            },
            color: '#1e293b',
          },
          grid: {
            display: false,
          },
        },
        y: {
          ticks: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 11,
            },
            color: '#64748b',
          },
          grid: {
            color: 'rgba(0, 0, 0, 0.05)',
          },
        },
      },
      plugins: {
        legend: {
          position: 'bottom',
          labels: {
            font: {
              family: "'DM Sans', sans-serif",
              size: 12,
            },
            padding: 15,
            usePointStyle: true,
          },
        },
        tooltip: {
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          titleFont: {
            family: "'DM Sans', sans-serif",
            size: 12,
          },
          bodyFont: {
            family: "'DM Sans', sans-serif",
            size: 11,
          },
        },
      },
    },
  };

  charts.frequentSkills = new Chart(ctx, config);
}

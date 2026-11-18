
const assetsContainer = document.getElementById('assets-container');
const tabsContainer = document.getElementById('tabs');
const totalDisplay = document.getElementById('total');

let allAssets = {};
let activeType = '';

// Получить все активы
async function fetchAssets() {
    try {
        const res = await fetch('/api/v1/portfolio');
        if (!res.ok) throw new Error('Ошибка при загрузке активов');
        return await res.json();
    } catch (err) {
        alert('Не удалось загрузить активы');
        console.error(err);
    }
}

// Получить общий баланс
async function fetchTotal() {
    try {
        const res = await fetch('/api/v1/portfolio/total');
        if (!res.ok) throw new Error('Ошибка при загрузке общего баланса');
        const total = await res.text();
        totalDisplay.textContent = `Общая стоимость: ${parseFloat(total).toFixed(2)} ₽`;
    } catch (err) {
        console.error(err);
    }
}

// Отобразить активы определенного типа
function showAssets(type) {
    assetsContainer.innerHTML = '';
    const assets = allAssets[type] || [];

    if (assets.length === 0) {
        assetsContainer.innerHTML = '<p>Нет активов этого типа.</p>';
        return;
    }

    assets.forEach(asset => {
        const card = document.createElement('div');
        card.className = 'asset-card';

        const growthClass = asset.growthInCurrencyUnits >= 0 ? 'green' : 'red';

        card.innerHTML = `
      <h3>${asset.name}</h3>
      <div class="asset-info"><strong>Средняя цена:</strong> ${asset.averageBuyPrice.toFixed(2)} ₽</div>
      <div class="asset-info"><strong>Текущая цена:</strong> ${asset.currentPricePerUnit.toFixed(2)} ₽</div>
      <div class="asset-info"><strong>Рост:</strong> <span class="${growthClass}">${asset.growthInPercents.toFixed(2)}%</span></div>
      <div class="asset-info"><strong>Изменение:</strong> <span class="${growthClass}">${asset.growthInCurrencyUnits.toFixed(2)} ₽</span></div>
      <div class="asset-info"><small>Обновлено: ${new Date(asset.lastUpdated).toLocaleString()}</small></div>
    `;
        assetsContainer.appendChild(card);
    });
}

// Обновить табы
function updateTabs(types) {
    tabsContainer.innerHTML = '';
    types.forEach(type => {
        const tab = document.createElement('div');
        tab.className = 'tab';
        if (type === activeType) tab.classList.add('active');
        tab.textContent = type;
        tab.onclick = () => {
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            activeType = type;
            showAssets(type);
        };
        tabsContainer.appendChild(tab);
    });

    if (types.length > 0 && !activeType) {
        activeType = types[0];
        document.querySelector('.tab').classList.add('active');
        showAssets(activeType);
    }
}

// Инициализация
async function init() {
    allAssets = await fetchAssets();
    await fetchTotal();

    const assetTypes = Object.keys(allAssets);
    updateTabs(assetTypes);
}

// Запускаем при загрузке скрипта
init();
document.addEventListener("DOMContentLoaded", () => {
    // === DOM Elements ===
    const assetsContainer = document.getElementById('assets-container');
    const tabsContainer = document.getElementById('tabs');
    const totalDisplay = document.getElementById('total');
    const growthDisplay = document.getElementById('growth');

    const openModalBtn = document.getElementById('openModalBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const modal = document.getElementById('assetModal');

    const showStockFormBtn = document.getElementById('showStockFormBtn');
    const showDepositFormBtn = document.getElementById('showDepositFormBtn');

    const stockForm = document.getElementById('stockForm');
    const depositForm = document.getElementById('depositForm');

    let allAssets = {};
    let activeType = '';

    // === Functions ===

    function hideAllForms() {
        if (stockForm) stockForm.classList.add('hidden');
        if (depositForm) depositForm.classList.add('hidden');
    }

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

    async function fetchTotal() {
        try {
            const res = await fetch('/api/v1/portfolio/total');
            if (!res.ok) throw new Error('Ошибка при загрузке общего баланса');
            const total = await res.text();
            totalDisplay.textContent = `${parseFloat(total).toFixed(2)} ₽`;
        } catch (err) {
            console.error(err);
        }
    }

    function showAssets(type) {
        assetsContainer.innerHTML = '';
        const assets = allAssets[type] || [];
        if (assets.length === 0) {
            assetsContainer.innerHTML = '<p>Нет активов этого типа.</p>';
            return;
        }

        let totalGrowth = 0;
        assets.forEach(asset => {
            const growthPercent = parseFloat(asset.growthInPercents.toFixed(2));
            totalGrowth += growthPercent;

            const item = document.createElement('div');
            item.className = 'asset-card';
            const growthClass = asset.growthInCurrencyUnits >= 0 ? 'positive' : 'negative';
            item.innerHTML = `
                <h3>${asset.name}</h3>
                <div class="asset-info"><strong>Средняя цена:</strong> ${asset.averageBuyPrice.toFixed(2)} ₽</div>
                <div class="asset-info"><strong>Текущая цена:</strong> ${asset.currentPricePerUnit.toFixed(2)} ₽</div>
                <div class="asset-info"><strong>Рост:</strong> <span class="${growthClass}">${growthPercent}%</span></div>
                <div class="asset-info"><strong>Изменение:</strong> <span class="${growthClass}">${asset.growthInCurrencyUnits.toFixed(2)} ₽</span></div>
                <div class="asset-info"><small>Обновлено: ${new Date(asset.lastUpdated).toLocaleString()}</small></div>
            `;
            assetsContainer.appendChild(item);
        });

        const avgGrowth = totalGrowth / assets.length;
        growthDisplay.textContent = `${avgGrowth > 0 ? '+' : ''}${avgGrowth.toFixed(2)}%`;
        growthDisplay.classList.remove('positive', 'negative');
        growthDisplay.classList.add(avgGrowth >= 0 ? 'positive' : 'negative');
    }

    function updateTabs(types) {
        tabsContainer.innerHTML = '';
        types.forEach(type => {
            const tab = document.createElement('a');
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

    async function init() {
        allAssets = await fetchAssets();
        await fetchTotal();
        const assetTypes = Object.keys(allAssets);
        updateTabs(assetTypes);
        showAssets(assetTypes[0]);
    }

    async function refreshData() {
        allAssets = await fetchAssets();
        await fetchTotal();
        updateTabs(Object.keys(allAssets));
        showAssets(activeType);
    }

    // === Event Listeners ===

    if (openModalBtn) {
        openModalBtn.addEventListener('click', () => {
            modal.style.display = 'block';
        });
    }

    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', () => {
            modal.style.display = 'none';
            hideAllForms();
        });
    }

    if (showStockFormBtn && stockForm) {
        showStockFormBtn.addEventListener('click', () => {
            hideAllForms();
            stockForm.classList.remove('hidden');
        });
    }

    if (showDepositFormBtn && depositForm) {
        showDepositFormBtn.addEventListener('click', () => {
            hideAllForms();
            depositForm.classList.remove('hidden');
        });
    }

    if (stockForm) {
        stockForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(stockForm);
            const data = Object.fromEntries(formData);

            try {
                const res = await fetch('/api/v1/portfolio/stock/save', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                if (res.ok) {
                    alert('Акция успешно добавлена!');
                    refreshData();
                    closeModalBtn.click();
                } else {
                    alert('Ошибка при добавлении акции');
                }
            } catch (err) {
                console.error(err);
                alert('Не удалось отправить данные');
            }
        });
    }

    if (depositForm) {
        depositForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(depositForm);
            const data = Object.fromEntries(formData);

            try {
                const res = await fetch('/api/v1/portfolio/deposit/save', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                if (res.ok) {
                    alert('Депозит успешно добавлен!');
                    refreshData();
                    closeModalBtn.click();
                } else {
                    alert('Ошибка при добавлении депозита');
                }
            } catch (err) {
                console.error(err);
                alert('Не удалось отправить данные');
            }
        });
    }

    // === Init ===
    init();
});
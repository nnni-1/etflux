document.addEventListener("DOMContentLoaded", () => {
    // URL вашего контроллера
    const apiUrl = "/portfolio/assets";

    // Функция для отправки fetch-запроса
    async function fetchAssets() {
        try {
            const response = await fetch(apiUrl, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }

            const data = await response.json();
            renderAssets(data);
        } catch (error) {
            console.error("Ошибка при получении данных:", error);
            alert("Не удалось загрузить данные. Попробуйте позже.");
        }
    }

    // Функция для отображения активов
    function renderAssets(assets) {
        for (const [assetTypeName, assetList] of Object.entries(assets)) {
            const containerId = getContainerId(assetTypeName);
            const container = document.getElementById(containerId);

            if (container) {
                container.innerHTML = ""; // Очищаем контейнер

                assetList.forEach(asset => {
                    const item = createPortfolioItem(asset, assetTypeName);
                    container.appendChild(item);
                });
            }
        }
    }

    // Функция для создания HTML-элемента актива
    function createPortfolioItem(asset, assetTypeName) {
        const item = document.createElement("div");
        item.classList.add("portfolio-item");

        if (assetTypeName.toLowerCase() === "bank_deposit") {
            // Отображение банковского вклада
            const nameSpan = document.createElement("span");
            nameSpan.textContent = `${asset.name}`;
            item.appendChild(nameSpan);

            const valueSpan = document.createElement("span");
            valueSpan.classList.add("value");
            valueSpan.textContent = `${formatCurrency(asset.quantity)}₽`;
            item.appendChild(valueSpan);

            const percentageSpan = document.createElement("span");
            percentageSpan.classList.add("percentage");
            percentageSpan.classList.add("positive"); // Вклады всегда положительные
            percentageSpan.textContent = `+${asset.asset.interestRate}% годовых`;
            item.appendChild(percentageSpan);
        } else {
            // Отображение других активов (акции, облигации и т.д.)
            const img = document.createElement("img");
            img.src = asset.asset.icon || "https://via.placeholder.com/30";
            img.alt = asset.name;
            item.appendChild(img);

            const nameSpan = document.createElement("span");
            nameSpan.innerHTML = `${asset.name}<br><small>${asset.asset.ticker}</small>`;
            item.appendChild(nameSpan);

            const separator = document.createElement("span");
            separator.textContent = "...";
            item.appendChild(separator);

            const valueSpan = document.createElement("span");
            valueSpan.classList.add("value");
            valueSpan.textContent = `${formatCurrency(asset.averageBuyPrice)}₽ → ${formatCurrency(asset.currentPricePerUnit)}₽`;
            item.appendChild(valueSpan);

            const percentageSpan = document.createElement("span");
            percentageSpan.classList.add("percentage");
            percentageSpan.classList.add(asset.growthInPercents >= 0 ? "positive" : "negative");
            percentageSpan.textContent = `${asset.growthInPercents.toFixed(2)}%`;
            item.appendChild(percentageSpan);
        }

        return item;
    }

    // Вспомогательная функция для форматирования валюты
    function formatCurrency(value) {
        return value.toLocaleString("ru-RU", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }

    // Функция для определения ID контейнера по типу актива
    function getContainerId(assetTypeName) {
        switch (assetTypeName) {
            case "STOCKS":
                return "stocks";
            case "BOND":
                return "bonds";
            case "CRYPTO_CURRENCY":
                return "crypto";
            case "FUND":
                return "funds";
            case "BANK_DEPOSIT":
                return "deposits";
            default:
                return null;
        }
    }

    // Загружаем данные при первой загрузке страницы
    fetchAssets();
});
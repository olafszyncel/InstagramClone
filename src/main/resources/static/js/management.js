async function managementSearch(role) {
    const query = document.querySelector(".management-search").value.trim();
    const resultsContainer = document.querySelector(".search-management-result-container");

    if (query.length < 1) {
        resultsContainer.innerHTML = "";
        return;
    }

    try {
        const response = await fetch(`/management/search/${role}?query=${encodeURIComponent(query)}`);
        const users = await response.json();

        resultsContainer.innerHTML = ``;

        users.forEach(user => {
            let resultBox = document.createElement("div");
            resultBox.classList.add("management-result-box");
            let buttonText = "";
            if(role === 'ban') {
                buttonText = user[1] === "BANNED" ? "Unban" : "Ban";
            } else if(role === 'mod') {
                buttonText = user[1] === "MOD" ? "Unmod" : "Mod";
            }

            resultBox.innerHTML =  `
                                <div class="management-result-text">${user[0]}</div>
                                <button class="block-button" onclick="changeRoleHandler('${user[0]}', '${role}')" id="${role}-button-${user[0]}">${buttonText}</button>`;

            resultsContainer.appendChild(resultBox);
        });

    } catch (error) {
        console.error("Error fetching search results:", error);
    }
}

function changeRoleHandler(user, role) {
    const button  = document.getElementById(`${role}-button-${user}`)

    fetch(`/management/${user}/${role}`, {method: 'POST'})
        .then(response => response.json)
        .then(() => {
            if(button.innerText === `Un${role}`) {
                button.innerText = role;
            } else {
                button.innerText =`Un${role}`;
            }

        });
}

let modsPage = 0;
let modsPageSize = 3;

function loadAllMods(pageDiff) {
    const resultsContainer = document.querySelector(".management-result-container");
    const previousButton = document.getElementById("previous-mods-page");
    const nextButton = document.getElementById("next-mods-page");
    modsPage += pageDiff;

    if(modsPage === 0) {
        previousButton.classList.add("disabled");
    } else {
        previousButton.classList.remove("disabled");
    }

    fetch(`/management/search/all/mods?page=${modsPage}&size=${modsPageSize}`)
        .then(response => response.json())
        .then(result => {


            let list = result.content;
            if(list.length > 0) {
                resultsContainer.innerHTML = "";
                nextButton.classList.remove("disabled");
                let modBox;
                list.forEach(mod => {
                    modBox = document.createElement("div");
                    modBox.classList.add("management-result-box");

                    modBox.innerHTML =  `<div class="management-result-text">${mod}</div>`
                    resultsContainer.appendChild(modBox);
                })
                fetch(`/management/search/all/mods?page=${modsPage+1}&size=${modsPageSize}`)
                    .then(response => response.json())
                    .then(result => {
                        if (result.content.length <= 0) {
                            nextButton.classList.add("disabled");
                        }
                    })
            } else if(list.length < modsPageSize){
                nextButton.classList.add("disabled");
            } else {
                nextButton.classList.add("disabled");
            }
        });
}

document.addEventListener("DOMContentLoaded", () => {
    loadAllMods(0);
})
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

        })
}
// SEARCHING FUNCTION
async function searchUsers() {
    const query = document.getElementById("searchInput").value.trim();
    const resultsContainer = document.getElementById("searchResults");

    if (query.length < 1) {
        resultsContainer.innerHTML = "";
        return;
    }

    try {
        const response = await fetch(`/search/profile?query=${encodeURIComponent(query)}`);
        const users = await response.json();

        resultsContainer.innerHTML = users.length
            ? users.map(user => `<div class="search-result-link">
                                            <a href="/profile/${user}" >
                                                <div class="search-text-link">${user}</div>
                                            </a>
                                        </div>`).join("")
            : `<div class="search-result-link"><div class="search-text-not-found">No results found</div></div>`;

    } catch (error) {
        console.error("Error fetching search results:", error);
    }
}

// FUNCTION TO MAKE COMMENT INPUT LOOKING GOOD
function resizeTextarea(textarea) {
    textarea.style.height = "28px";
    textarea.style.height = textarea.scrollHeight + "px";

    //const charCount = document.getElementById("charCount");
    //charCount.textContent = textarea.value.length + " / 250";
}

// FUNCTION THAT OPENS DROPDOWN LIST WITH OPTION TO EDIT/DELETE POST/COMMENTS
function toggleDropdown(element) {
    element.style.display = element.style.display === "block" ? "" : "block";
    let dropdown = element.nextElementSibling;
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";

    document.querySelectorAll(".dropdown").forEach(el => {
        if (el !== dropdown) el.style.display = "none";
    });
}

document.addEventListener("click", function (event) {
    if (!event.target.matches('.d')) {
        document.querySelectorAll(".dropdown").forEach(el => el.style.display = "none");
    }
});


document.addEventListener("DOMContentLoaded", function () {
    const searchToggle = document.querySelector(".search-toggle");
    const closeButton = document.querySelector(".close-search");


    if (window.innerWidth <= 768) {
        searchToggle.addEventListener("click", function () {
            mobileSearchBar("search");
        });
        closeButton.addEventListener("click", function () {
           mobileSearchBar("close");
        })
    }
});

// FUNCTION TO MAKE DIFFERENT SEARCHING VIEW IN MOBILE
function mobileSearchBar(button) {
    const searchInput = document.querySelector(".search-input");
    const searchContainer = document.querySelector(".search-container");
    const searchResultContainer = document.querySelector(".search-result-container");
    const body = document.querySelector("body");
    const closeButton = document.querySelector(".close-search");

    if(button === "search") {
        searchContainer.classList.toggle("active");
        searchInput.classList.toggle("active");
        searchResultContainer.classList.toggle("active");
        body.classList.toggle("active");
        closeButton.classList.toggle("active");
    } else if(button === "close") {
        searchContainer.classList.toggle("active");
        searchInput.classList.toggle("active");
        searchResultContainer.classList.toggle("active");
        body.classList.toggle("active");
        closeButton.classList.toggle("active");
    }
}




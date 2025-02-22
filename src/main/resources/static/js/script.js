function likeHandler(postId) {
    const btn = document.getElementById(postId);
    const likes= document.getElementById(`likes-number-${postId}`);
    let LikeOrUnlike = btn.innerText;



    if(LikeOrUnlike === "Like") {
        // trzeba dodac like
        fetch(`/like/${postId}`, {method: 'POST'})
            .then(response => response.json)
            .then(result => {
                btn.innerText = "Liked";
                likes.innerText = `${parseInt(likes.innerText) + 1}`;
            })
    } else {
        // trzeba usunac like
        fetch(`/unlike/${postId}`, {method: 'POST'})
            .then(response => response.json)
            .then(result => {
                btn.innerText = "Like";
                calculating = parseInt(likes.innerText) - 1;
                likes.innerText = calculating;
            })
    }
}

function followHandler(username) {
    const btn = document.getElementById("followButton");
    const follows = document.getElementById("followersCount");
    let FollowOrUnfollow = btn.innerText;

    username = username.replaceAll("&#39;", "'");

    fetch(`/follow/${username}`, {method: 'POST'})
        .then(response => response.json)
        .then(result => {

            if(FollowOrUnfollow === "Follow") {
                btn.innerText = "Following";
                follows.innerText = `${parseInt(follows.innerText) + 1}`;
            } else {
                btn.innerText = "Follow";
                follows.innerText = `${parseInt(follows.innerText) - 1}`;
            }

        })
}

function commentHandler(postId) {
    let commentContent = document.getElementById(`comment-input-${postId}`).value;
    const username = document.getElementById(`comment-username-${postId}`).value;
    const commentData = {
        content: commentContent,
        userId: document.getElementById(`comment-user-${postId}`).value
    };

    fetch(`/comment/${postId}/new`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(commentData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Błąd podczas wysyłania komentarza');
            }
        })
        .then(data => {
            document.getElementById(`comment-input-${postId}`).value = '';
            afterCommentAdding(commentContent, username, postId, data.id);
        })
        .catch(error => {
            console.error('Błąd:', error);
        });

}

function afterCommentAdding(content, username, postId, commentId) {
    const commentDiv = document.createElement("div");
    commentDiv.classList.add("comment");
    commentDiv.id = `comment-${commentId}`;

    const commentUser = commentDiv.appendChild(document.createElement("a"));
    commentUser.classList.add("comment-user");
    commentUser.innerText = username;
    commentUser.href = `/profile/${username}`;

    const commentDropdown = commentDiv.appendChild(document.createElement("ul"));
    commentDropdown.classList.add("dropdown")
    const commentDropdownLi = commentDropdown.appendChild(document.createElement("li"));
    const commentDropdownLink = commentDropdownLi.appendChild(document.createElement("a"));
    commentDropdownLink.innerText = "delete";
    commentDropdownLink.onclick = function() {deleteCommentHandler(commentId)};

    const commentContent = commentDiv.appendChild(document.createElement("p"));
    commentContent.classList.add("comment-content");
    commentContent.innerText = content;

    const commentsContainer = document.querySelector(`#comments-${postId}`);
    if (!commentsContainer) {
        return;
    }
    commentsContainer.prepend(commentDiv);
}

function deleteCommentHandler(commentId) {
    const box = document.getElementById(`comment-${commentId}`);

    fetch(`/comment/${commentId}/delete`, {method: "POST"})
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Błąd podczas usuwania komentarza');
            }
        })
        .then(data => {
            box.style.display = 'none';

        })
        .catch(error => {
            console.error('Błąd:', error);
        });

}

async function searchUsers() {
    const query = document.getElementById("searchInput").value.trim();
    const resultsContainer = document.getElementById("searchResults");

    if (query.length < 1) {
        resultsContainer.innerHTML = "";
        resultsContainer.classList.add("hidden");
        return;
    }

    try {
        const response = await fetch(`/search/profile?query=${encodeURIComponent(query)}`);
        const users = await response.json();

        resultsContainer.innerHTML = users.length
            ? users.map(user => `<div class="p-3 hover:bg-gray-100 border-b">
                                            <a href="/profile/${user}" class="block text-blue-600">${user}</a>
                                        </div>`).join("")
            : `<div class="p-3 text-gray-500">No results found</div>`;

        resultsContainer.classList.remove("hidden");
    } catch (error) {
        console.error("Error fetching search results:", error);
    }
}


document.getElementById("searchInput").addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        event.preventDefault();
    }
});


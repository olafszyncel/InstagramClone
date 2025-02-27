// CREATE COMMENT
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

// DELETE COMMENT
function deleteCommentHandler(commentId) {
    const box = document.getElementById(`comment-${commentId}`);
    const commentsNumber = document.getElementById("comments-number");


    fetch(`/comment/${commentId}/delete`, {method: "POST"})
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Błąd podczas usuwania komentarza');
            }
        })
        .then(() => {
            box.style.display = 'none';
            commentsNumber.innerText = `${parseInt(commentsNumber.innerText) - 1}`;
        })
        .catch(error => {
            console.error('Błąd:', error);
        });

}

// CREATES COMMENT DIV JUST AFTER CREATING (IF SUCCESS)
function afterCommentAdding(content, username, postId, commentId) {
    const commentsNumber = document.getElementById("comments-number");
    commentsNumber.innerText = `${parseInt(commentsNumber.innerText) + 1}`;

    const commentDiv = document.createElement("div");
    commentDiv.classList.add("comment");
    commentDiv.id = `comment-${commentId}`;

    const commentUser = commentDiv.appendChild(document.createElement("a"));
    commentUser.classList.add("comment-user");
    commentUser.innerText = "@" + username;
    commentUser.href = `/profile/${username}`;


    const commentContent = commentDiv.appendChild(document.createElement("p"));
    commentContent.classList.add("comment-content");
    commentContent.innerText = content;

    const commentManagement = commentDiv.appendChild(document.createElement("div"));
    commentManagement.classList.add("comment-dots");
    commentManagement.innerHTML = `<span class="dots d" onClick="toggleDropdown(this)">&#x22EE;</span>
                                   <ul class="dropdown">
                                        <li><a onclick='deleteCommentHandler(${commentId})'>delete</a></li>
                                    </ul>`;


    const commentsContainer = document.querySelector(`#comments-${postId}`);
    if (!commentsContainer) {
        return;
    }
    commentsContainer.prepend(commentDiv);
}
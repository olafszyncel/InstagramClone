function likeHandler(postId) {
    const btn = document.getElementById(`${postId}`);
    const likes= document.getElementById(`likes-number-${postId}`);
    console.log(likes.innerText);
    LikeOrUnlike = btn.innerText;



    if(LikeOrUnlike === "Like") {
        // trzeba dodac like
        fetch(`/like/${postId}`)
            .then(response => response.json)
            .then(result => {
                btn.innerText = "Liked";
                calculating = parseInt(likes.innerText) + 1;
                likes.innerText = calculating;
            })
    } else {
        // trzeba usunac like
        fetch(`/unlike/${postId}`)
            .then(response => response.json)
            .then(result => {
                btn.innerText = "Like";
                calculating = parseInt(likes.innerText) - 1;
                likes.innerText = calculating;
            })
    }
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
            console.log('Komentarz został zapisany:', data);
            document.getElementById(`comment-input-${postId}`).value = '';
            afterCommentAdding(commentContent, username, postId);
        })
        .catch(error => {
            console.error('Błąd:', error);
        });

}

function afterCommentAdding(content, username, postId) {
    const commentDiv = document.createElement("div");
    commentDiv.classList.add("comment");

    const commentUser = commentDiv.appendChild(document.createElement("a"));
    commentUser.classList.add("comment-user");
    commentUser.innerText = username;
    commentUser.href = `/profile/${username}`;

    const commentContent = commentDiv.appendChild(document.createElement("p"));
    commentContent.classList.add("comment-content");
    commentContent.innerText = content;

    commentDiv.appendChild(commentUser);
    commentDiv.appendChild(commentContent);

    const commentsContainer = document.querySelector(`#comments-${postId}`);
    if (!commentsContainer) {
        console.error(`Nie znaleziono elementu z klasą .comments-${postId}`);
        return;
    }
    commentsContainer.prepend(commentDiv);
}
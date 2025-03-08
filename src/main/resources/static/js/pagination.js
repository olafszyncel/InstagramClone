let page = 0;
let loading = false;
let userId;


window.addEventListener('scroll', handleScroll);

function handleScroll() {
    if (((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 200) && !loading) {
        loadMorePosts(window.location.pathname);
    }
}

document.addEventListener("DOMContentLoaded", function() {
    if(window.location.pathname.split("/")[1] === "post") {
        window.removeEventListener('scroll', handleScroll);
        singlePostHandler()
    } else {
        userId = document.getElementById("user-id").value;
        loadMorePosts(window.location.pathname);
    }

});

function whenCreated(createdOn) {
    const todayDate = new Date();
    const postDate = new Date(createdOn);
    let timeInMinutes = (todayDate.getTime() - postDate.getTime()) / 60000;

    const rtf = new Intl.RelativeTimeFormat('en', { numeric: 'auto' });

    if(timeInMinutes < 60) {
        return rtf.format(-Math.floor(timeInMinutes), 'minute');
    }
    else if(timeInMinutes / 60 < 24){
        return rtf.format(-Math.floor(timeInMinutes / 60), 'hour'); //hours
    }
    else if(timeInMinutes / 1440 < 7) {
        return rtf.format(-Math.floor(timeInMinutes / 1440), 'day'); //days
    }
    else {
        return rtf.format(-Math.floor(timeInMinutes / 10080), 'week'); //weeks
    }
}


function loadMorePosts(path) {
    loading = true;
    document.getElementById('loading').style.display = 'block';
    path = path === "/" ? "" : path;

    fetch(`${path}/posts?page=${page}&size=5`)
        .then(response => response.json())
        .then(result => {
            let posts = result.content;
            if (posts.length > 0) {


                posts.forEach(post => {

                    const postElement = document.createElement('div');
                    postElement.classList.add('feed-container');
                    let stringElement;
                    let likeElement, likesNumber, commentElement;

                    if(post.likes?.some(like => Number(like['user']['id']) === Number(userId))) {
                        likeElement = `
                                            <div>
                                                <button class="like-button" name="liked" onclick="likeHandler('${post.id}')" 
                                                id="${post.id}" >
                                                    <svg xmlns="http://www.w3.org/2000/svg" id="Filled" viewBox="0 0 24 24">
                                                        <path fill="currentColor" d="M17.5,1.917a6.4,6.4,0,0,0-5.5,3.3,6.4,
                                                        6.4,0,0,0-5.5-3.3A6.8,6.8,0,0,0,0,8.967c0,4.547,4.786,9.513,
                                                        8.8,12.88a4.974,4.974,0,0,0,6.4,0C19.214,18.48,24,13.514,24,
                                                        8.967A6.8,6.8,0,0,0,17.5,1.917Z"/>
                                                    </svg>
                                                </button>
                                            </div>`;
                    } else {

                        likeElement = `
                                            <div>
                                                <button class="like-button" name="like" onclick="likeHandler('${post.id}')" 
                                                id="${post.id}" >
                                                    <svg xmlns="http://www.w3.org/2000/svg" id="Bold" viewBox="0 0 24 24">
                                                        <path d="M17.25,1.851A6.568,6.568,0,0,0,12,4.558,6.568,6.568,0,0,0,6.75,
                                                        1.851,7.035,7.035,0,0,0,0,9.126c0,4.552,4.674,9.425,8.6,12.712a5.29,5.29,
                                                        0,0,0,6.809,0c3.922-3.287,8.6-8.16,8.6-12.712A7.035,7.035,0,0,0,17.25,
                                                        1.851ZM13.477,19.539a2.294,2.294,0,0,1-2.955,0C5.742,15.531,3,11.736,3,
                                                        9.126A4.043,4.043,0,0,1,6.75,4.851,4.043,4.043,0,0,1,10.5,9.126a1.5,1.5,
                                                        0,0,0,3,0,4.043,4.043,0,0,1,3.75-4.275A4.043,4.043,0,0,1,21,9.126C21,
                                                        11.736,18.258,15.531,13.477,19.539Z"/>
                                                    </svg>
                                                </button>
                                            </div>`;
                    }
                    likesNumber = `
                                            <div class="likes-number">
                                                <span id="likes-number-${post.id}" >${post.likes.length}</span>
                                            </div>`;
                    commentElement = `
                                            <a class="like-item" href="/post/${post.id}">
                                                <div class="like-button">
                                                    <svg xmlns="http://www.w3.org/2000/svg" id="Isolation_Mode" 
                                                    data-name="Isolation Mode" viewBox="0 0 24 24">
                                                        <path d="M24,24H12.017A12,12,0,1,1,24,11.246l0,
                                                        .094ZM12.017,3a9,9,0,1,0,0,18H21V11.389A9.015,
                                                        9.015,0,0,0,12.017,3Z"/>
                                                    </svg>
                                                </div>
                                            </a>
                        
                                            <div class="likes-number">
                                                <span>${post.comments.length}</span>
                                            </div>
                    `
                    stringElement = `
                                 <div class="post">
                                     <div class="post-header">
                                         <a class="comment-user username" href="/profile/${post.user.username}">
                                             <span>@${post.user.username}</span>
                                         </a>
                                         <span class="date">${whenCreated(post.createdOn)}</span>
                                     </div>
                                     <a href="/post/${post.id}">
                                         <img src="${post.photoUrl}" alt="Post image" class="post-image">
                                     </a>
                                     <div class="post-description">
                                         <a href="/profile/${post.user.username}">
                                             <span style="font-weight: bold;">${post.user.username}</span>
                                         </a>
                                        <span>${post.content}</span>
                                     </div>
                                     <div class="like-box">
                        
                                        <div class="like-item">`
                                        + likeElement + likesNumber +
                                       `</div>`
                                       + commentElement +
                                    `</div>
                                 </div>
                        `;
                    postElement.innerHTML = stringElement;
                    document.getElementById('post-container').appendChild(postElement);
                });
                page++;
            } else {
                window.removeEventListener('scroll', handleScroll);
            }
        })
        .finally(() => {
            loading = false;
            document.getElementById('loading').style.display = 'none';
        });
}

// handle post in path /post/postId
function singlePostHandler() {
    const htmlElement = document.querySelector(".date");
    const createdOn = htmlElement.innerText;
    htmlElement.innerText = whenCreated(createdOn);
}
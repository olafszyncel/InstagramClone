function followHandler(username) {
    const btn = document.getElementById("followButton");
    const follows = document.getElementById("followersCount");
    let FollowOrUnfollow = btn.innerText;

    username = username.replaceAll("&#39;", "'");

    fetch(`/follow/${username}`, {method: 'POST'})
        .then(response => response.json)
        .then(() => {

            if(FollowOrUnfollow === "Follow") {
                btn.innerText = "Following";
                follows.innerText = `${parseInt(follows.innerText) + 1}`;
            } else {
                btn.innerText = "Follow";
                follows.innerText = `${parseInt(follows.innerText) - 1}`;
            }

        })
}
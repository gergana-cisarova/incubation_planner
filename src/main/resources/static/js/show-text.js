function textHandler() {

    let button = document.querySelector("#hide")

    function handler(e) {
        let hiddenText = e.target.parentNode.querySelector("#hiddenInfo");
        action[e.target.innerText](e, hiddenText);
    }

    const action = {
        "Show Lab occupancy": (e, show) => {
            show.style.display = "block";
            e.target.innerText = "Hide Lab occupancy";
        },
        "Hide Lab occupancy": (e, show) => {
            show.style.display = "none";
            e.target.innerText = "Show Lab occupancy";
        }

    }
    button.addEventListener("click", handler)

}
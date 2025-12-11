function runAnalysis() {
    const choice = document.getElementById("choice").value;

    fetch(`http://localhost:3000/run?choice=${choice}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("output").textContent =
                JSON.stringify(data, null, 2);
        })
        .catch(err => {
            document.getElementById("output").textContent = "Error: " + err;
        });
}

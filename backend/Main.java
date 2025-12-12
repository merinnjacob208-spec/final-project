const express = require('express');
const fs = require('fs');
const app = express();
const PORT = 3000;

app.get('/run', (req, res) => {
    const choice = parseInt(req.query.choice);

    if (!choice) {
        return res.json({ error: "Missing choice parameter" });
    }

    fs.readFile("data.csv", "utf8", (err, data) => {
        if (err) {
            return res.json({ error: "data.csv not found" });
        }

        const lines = data.trim().split("\n");
        lines.shift(); // Remove header

        let totalPeople = 0;
        let totalCarAccident = 0;
        let totalSleepHours = 0;
        let totalStress = 0;

        let groupPeople = 0;
        let groupAccidents = 0;
        let groupStress = 0;

        for (const line of lines) {
            const parts = line.split(",");

            const sleepHours = parseFloat(parts[1]);
            const carAccidents = parseInt(parts[2]);
            const stressLevel = parseInt(parts[3]);

            totalPeople++;
            totalCarAccident += carAccidents;
            totalSleepHours += sleepHours;
            totalStress += stressLevel;

            let matches = false;

            if (choice === 1 && sleepHours < 6) matches = true;
            if (choice === 2 && sleepHours > 6) matches = true;
            if (choice === 3 && stressLevel < 5) matches = true;
            if (choice === 4 && stressLevel >= 5) matches = true;

            if (matches) {
                groupPeople++;
                groupAccidents += carAccidents;
                groupStress += stressLevel;
            }
        }

        res.json({
            totalPeople,
            avgSleep: totalSleepHours / totalPeople,
            avgAccidents: totalCarAccident / totalPeople,
            avgStress: totalStress / totalPeople,
            groupPeople,
            groupAccidents: groupPeople? (groupAccidents / groupPeople) : 0,
            groupStress: (choice === 3 || choice === 4)
                ? (groupPeople ? (groupStress / groupPeople) : 0)
                : 0
        });
    });
});

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});

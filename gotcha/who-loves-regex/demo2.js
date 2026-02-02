export function initEmailRegexDemo() {
    // Evil email validation regex (catastrophic backtracking)
    const evilEmailRegex = /^([a-z]+)+@example\.com$/;

    const emailInput = document.getElementById("emailInput");
    const testBtn = document.getElementById("testBtn");
    const runPresetBtn = document.getElementById("runPresetBtn");
    const output = document.getElementById("output");
    const liveTimer = document.getElementById("liveTimer");

    function log(message) {
        output.textContent += message + "\n";
        output.scrollTop = output.scrollHeight;
    }

    function updateTimer(start) {
        liveTimer.textContent = (performance.now() - start).toFixed(2) + " ms";
    }

    function sleep(ms) {
        return new Promise(r => setTimeout(r, ms));
    }

    async function runTest(input) {
        log(`Testing input length ${input.length}...`);

        const start = performance.now();
        const interval = setInterval(() => updateTimer(start), 16);

        evilEmailRegex.test(input);

        clearInterval(interval);
        const time = (performance.now() - start).toFixed(2);
        liveTimer.textContent = time + " ms";

        log(`Time: ${time} ms\n`);
    }

    async function runPresetAttack() {
        const lengths = [10, 15, 20, 25, 30, 35, 36, 37, 38, 39, 40];

        testBtn.disabled = true;
        runPresetBtn.disabled = true;
        output.textContent = "";

        log("Running evil email attack...\n");

        for (const n of lengths) {
            const attackInput = "a".repeat(n) + "X";
            await runTest(attackInput);
            await sleep(300);
        }

        log("Done.");
        testBtn.disabled = false;
        runPresetBtn.disabled = false;
    }

    testBtn.addEventListener("click", () => {
        const value = emailInput.value;
        if (!value) return;
        runTest(value);
    });

    runPresetBtn.addEventListener("click", runPresetAttack);
}

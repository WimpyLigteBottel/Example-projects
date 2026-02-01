(function() {
    const evilEmailRegex = /^([a-z]+)+@example\.com$/;

    function testEvil(n) {
        const input = "a".repeat(n) + "X";
        const start = performance.now();
        evilEmailRegex.test(input);
        const end = performance.now();
        const time = (end - start).toFixed(2);
        console.log(`Length: ${n}, Time: ${time}ms`);
    }

    for (const n of [10, 15, 20, 25, 30, 35,36,37,38,39,40]) {
        testEvil(n);
    }
})();
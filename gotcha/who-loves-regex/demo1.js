(function() {
    const evilRegex = /^(a+)+b$/;
    
    function testEvil(n) {
        const input = "a".repeat(n) + "c";
        const start = performance.now();
        evilRegex.test(input);
        const end = performance.now();
        const time = (end - start).toFixed(2);
        console.log(`Length: ${n}, Time: ${time}ms`);
    }
    
    for (const n of [10, 12, 14, 16, 18, 20, 22, 24]) {
        testEvil(n);
    }
})();
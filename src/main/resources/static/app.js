document.addEventListener('DOMContentLoaded', () => {
    // Tab Switching
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));

            btn.classList.add('active');
            const targetId = btn.getAttribute('data-target');
            document.getElementById(targetId).classList.add('active');
            
            document.getElementById('result-container').classList.add('hidden');
            document.getElementById('status-result').classList.add('hidden');
        });
    });

    // Toast Notification System
    const toast = document.getElementById('toast');
    let toastTimeout;

    const showToast = (message, type = 'success') => {
        toast.textContent = message;
        toast.className = `toast show ${type}`;
        
        clearTimeout(toastTimeout);
        toastTimeout = setTimeout(() => {
            toast.classList.remove('show');
        }, 3000);
    };

    // Form Handling - Shorten URL
    const shortenForm = document.getElementById('shorten-form');
    const submitBtn = document.getElementById('submit-btn');
    const resultContainer = document.getElementById('result-container');
    const shortUrlLink = document.getElementById('short-url-link');
    const copyBtn = document.getElementById('copy-btn');

    shortenForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const originalUrl = document.getElementById('original-url').value;
        const customCode = document.getElementById('custom-code').value;
        const expiryAt = document.getElementById('expiry-at').value;

        const btnText = submitBtn.querySelector('span');
        const btnIcon = submitBtn.querySelector('i');
        const originalText = btnText.textContent;

        submitBtn.disabled = true;
        btnText.textContent = 'Generating...';
        btnIcon.className = 'fa-solid fa-spinner fa-spin';
        resultContainer.classList.add('hidden');

        try {
            const payload = { originalUrl: originalUrl };
            
            if (customCode.trim() !== '') {
                payload.customCode = customCode;
            }
            
            if (expiryAt) {
                const date = new Date(expiryAt);
                payload.expiryAt = date.toISOString();
            }

            const response = await fetch('/shortner/create-code', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const data = await response.json();

            if (!response.ok) {
                if (data.errors && Array.isArray(data.errors)) {
                    throw new Error(data.errors[0].defaultMessage || 'Validation failed');
                } else if (data.message) {
                    throw new Error(data.message);
                } else {
                    throw new Error('Failed to shorten URL');
                }
            }

            let shortUrl = data.shortUrl;
            if (!shortUrl) {
                const currentBase = window.location.origin;
                shortUrl = `${currentBase}/${data.shortCode}`;
            }

            shortUrlLink.href = shortUrl;
            shortUrlLink.textContent = shortUrl;
            resultContainer.classList.remove('hidden');
            showToast('Link generated successfully!');
            
        } catch (error) {
            showToast(error.message, 'error');
        } finally {
            submitBtn.disabled = false;
            btnText.textContent = originalText;
            btnIcon.className = 'fa-solid fa-arrow-right';
        }
    });

    // Copy functionality
    copyBtn.addEventListener('click', () => {
        const urlToCopy = shortUrlLink.textContent;
        navigator.clipboard.writeText(urlToCopy).then(() => {
            showToast('Copied to clipboard!');
            const icon = copyBtn.querySelector('i');
            icon.className = 'fa-solid fa-check';
            setTimeout(() => { icon.className = 'fa-regular fa-copy'; }, 2000);
        }).catch(() => {
            showToast('Failed to copy', 'error');
        });
    });

    // Form Handling - Status Check
    const statusForm = document.getElementById('status-form');
    const statusResult = document.getElementById('status-result');
    const statusBtn = statusForm.querySelector('button');

    statusForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        let code = document.getElementById('status-code').value.trim();
        if (code.startsWith('http')) {
            try {
                const urlObj = new URL(code);
                code = urlObj.pathname.substring(1);
            } catch (err) {}
        }

        const btnText = statusBtn.querySelector('span');
        const btnIcon = statusBtn.querySelector('i');
        const originalText = btnText.textContent;

        statusBtn.disabled = true;
        btnText.textContent = 'Loading...';
        btnIcon.className = 'fa-solid fa-spinner fa-spin';
        statusResult.classList.add('hidden');

        try {
            const response = await fetch(`/${code}/status`);

            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Link not found or expired');
                }
                const data = await response.json().catch(() => ({}));
                throw new Error(data.message || data.error || 'Failed to fetch status');
            }
            
            const data = await response.json();
            
            const formatDate = (dateString) => {
                if (!dateString) return 'Never';
                const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
                return new Date(dateString).toLocaleDateString(undefined, options);
            };

            document.getElementById('stat-clicks').textContent = data.clickCount || 0;
            document.getElementById('stat-created').textContent = formatDate(data.createdAt);
            document.getElementById('stat-expiry').textContent = formatDate(data.expiryAt);
            
            const origUrlEl = document.getElementById('stat-original');
            origUrlEl.href = data.originalUrl;
            origUrlEl.textContent = data.originalUrl;
            origUrlEl.title = data.originalUrl;

            statusResult.classList.remove('hidden');
            
        } catch (error) {
            showToast(error.message, 'error');
        } finally {
            statusBtn.disabled = false;
            btnText.textContent = originalText;
            btnIcon.className = 'fa-solid fa-arrow-right';
        }
    });
});

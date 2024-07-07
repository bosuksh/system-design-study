import asyncio


class AtomicCounter:
    def __init__(self, value=0):
        self.value = value
        self._lock = asyncio.Lock()

    async def increment(self):
        async with self._lock:
            self.value += 1
            return self.value

    async def decrement(self):
        async with self._lock:
            self.value -= 1
            return self.value

    async def get_value(self):
        async with self._lock:
            return self.value

    async def set_value(self, value):
        async with self._lock:
            self.value = value
            return self.value

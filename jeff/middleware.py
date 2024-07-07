from fastapi import FastAPI
from fastapi_utilities import repeat_every
from starlette.middleware.base import BaseHTTPMiddleware, RequestResponseEndpoint
from fastapi import Request
from starlette.responses import Response

from AtomicCounter import AtomicCounter
import asyncio


class TokenBucketRateLimitMiddleware(BaseHTTPMiddleware):
    TOKEN_BUCKET_SIZE = 4

    def __init__(self, app: FastAPI, counter: AtomicCounter = None):
        super().__init__(app)
        self.counter = counter

    async def dispatch(self, request: Request, call_next: RequestResponseEndpoint) -> Response:
        # Implement rate limiting here
        if await self.counter.get_value() <= 0:
            return Response("Rate limit exceeded", status_code=429)
        else:
            await self.counter.decrement()
            print(f"Token bucket: {await self.counter.get_value()}")
            response = await call_next(request)
            return response


class TokenRefillScheduler:
    TOKEN_BUCKET_SIZE = 4

    def __init__(self, counter: AtomicCounter):
        self.counter = counter

    @repeat_every(seconds=60)
    async def refill_bucket(self):
        await self.counter.set_value(self.TOKEN_BUCKET_SIZE)
        print(f"Refilled token bucket to {self.TOKEN_BUCKET_SIZE}")

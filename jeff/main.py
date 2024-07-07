from fastapi import FastAPI

from AtomicCounter import AtomicCounter
from middleware import TokenBucketRateLimitMiddleware, TokenRefillScheduler

app = FastAPI()
counter = AtomicCounter(4)
token_refill_scheduler = TokenRefillScheduler(counter)
app.add_middleware(TokenBucketRateLimitMiddleware, counter=counter)
app.add_event_handler("startup", token_refill_scheduler.refill_bucket)


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)

export namespace SessionStorage {
    export const get = (key: string): string | null => {
        if(typeof window === "undefined") return null
        const storage = window.sessionStorage
        return storage.getItem(key)
    }
    export const set = (key: string, value: string): void => {
        if(typeof window === "undefined") return
        const storage = window.sessionStorage
        storage.setItem(key, value)
    }
    export const remove = (key: string): void => {
        if(typeof window === "undefined") return
        const storage = window.sessionStorage
        storage.removeItem(key)
    }
    export const clear = (): void => {
        if(typeof window === "undefined") return
        const storage = window.sessionStorage
        storage.clear()
    }
}
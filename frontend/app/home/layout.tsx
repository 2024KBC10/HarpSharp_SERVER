"use client"

import AIIcon from "../(common)/(component)/(ai)/ai_icon";
import FixedMenu from "../(common)/(component)/(menu)/fixed_menu";

export default function HomeLayout({
    children,
}: Readonly<{ children: React.ReactNode }>) {
    return (
        <ReRenderProvider>
            <FixedMenu/>
            <AIIcon/>
            {children}
        </ReRenderProvider>
    )
}

import React, { createContext, useContext, useState, ReactNode } from 'react';

interface ReRenderContextProps {
    forceReRender: () => void
}

const ReRenderContext = createContext<ReRenderContextProps | undefined>(undefined)

interface ReRenderProviderProps {
    children: ReactNode
}

export const ReRenderProvider: React.FC<ReRenderProviderProps> = ({ children }) => {
    const [key, setKey] = useState(0);

    const forceReRender = () => setKey(prevKey => prevKey + 1)

    return (
        <ReRenderContext.Provider value={{ forceReRender }}>
            <div style={{ width: "100%", height: "100%", display: "flex" }} key={key}>
                {children}
            </div>
        </ReRenderContext.Provider>
    )
}

export const useReRender = (): ReRenderContextProps => {
    const context = useContext(ReRenderContext)
    if (!context) {
        throw new Error("잘못 된 접근 입니다.")
    }
    return context
}
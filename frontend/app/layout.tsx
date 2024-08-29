import type { Metadata } from "next";

import AppIcon from "../public/image/signature_icon.jpg"
import "./globals.css";

export const metadata: Metadata = {
  title: "KBC-Office",
  description: "우리는 개발자다.",
  icons: AppIcon.src,
  applicationName: "KBC-Office",
  authors: [
    {
      name: "root.song",
      url: "rmsdud2298@gmail.com",
    },
    {
      name: "magno.ryu",
      url: "ryoo0504@gmail.com",
    },
    {
      name: "juyeon.bae",
      url: "ilnyu0756@naver.com",
    },
    {
      name: "jayden.jeon",
      url: "astray642@gmail.com",
    },
    {
      name: "kangmin.han",
      url: "rkdalsdl112@gmail.com",
    },
  ],
  creator: "KBC Team",
  publisher: "Vercel",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        {children}
      </body>
    </html>
  );
}

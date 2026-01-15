#!/usr/bin/env python3
import os, re
from pathlib import Path

def remove_comments(content, file_type):
    """删除注释"""
    if file_type == 'java':
        # 删除多行注释
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        # 删除单行注释
        lines = content.split('\n')
        result = []
        for line in lines:
            if '//' in line:
                idx = line.find('//')
                line = line[:idx].rstrip()
            if line.strip() or not result:
                result.append(line)
        return '\n'.join(result)
    elif file_type in ['js', 'ts']:
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        lines = content.split('\n')
        result = []
        for line in lines:
            if '//' in line:
                idx = line.find('//')
                line = line[:idx].rstrip()
            if line.strip() or not result:
                result.append(line)
        return '\n'.join(result)
    elif file_type == 'vue':
        content = re.sub(r'<!--[\s\S]*?-->', '', content)
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        lines = content.split('\n')
        result = []
        for line in lines:
            if '//' in line:
                idx = line.find('//')
                line = line[:idx].rstrip()
            if line.strip() or not result:
                result.append(line)
        return '\n'.join(result)
    elif file_type == 'sql':
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        lines = content.split('\n')
        result = []
        for line in lines:
            if '--' in line:
                idx = line.find('--')
                line = line[:idx].rstrip()
            result.append(line)
        return '\n'.join(result)
    elif file_type in ['css', 'scss']:
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        return content
    return content

def update_author(content):
    """更新作者"""
    content = re.sub(r'@author\s+[^\n]*', '@author maoshun tian', content, flags=re.I)
    content = re.sub(r'"author"\s*:\s*"[^"]*"', '"author": "maoshun tian"', content, flags=re.I)
    content = re.sub(r"'author'\s*:\s*'[^']*'", "'author': 'maoshun tian'", content, flags=re.I)
    return content

def process_file(fpath):
    try:
        ext = fpath.suffix.lower().lstrip('.')
        if ext == 'java':
            ft = 'java'
        elif ext in ['js', 'ts', 'jsx', 'tsx']:
            ft = 'js'
        elif ext == 'vue':
            ft = 'vue'
        elif ext == 'sql':
            ft = 'sql'
        elif ext in ['css', 'scss']:
            ft = 'css'
        elif ext == 'vm':
            ft = 'java'
        else:
            ft = None
        
        with open(fpath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original = content
        if ft:
            content = remove_comments(content, ft)
        content = update_author(content)
        content = re.sub(r'\n{3,}', '\n\n', content)
        
        if content != original:
            with open(fpath, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except:
        return None

# 主程序
base = Path(__file__).parent
exclude = {'node_modules', '.git', 'target', 'dist', 'build', '.idea', '.vscode', '__pycache__', '.mvn'}
exts = ['.java', '.js', '.ts', '.vue', '.sql', '.scss', '.css', '.vm', '.json']

files = []
for root, dirs, filenames in os.walk(base):
    dirs[:] = [d for d in dirs if d not in exclude]
    for f in filenames:
        p = Path(root) / f
        if p.suffix.lower() in exts:
            files.append(p)

total = len(files)
done = 0
changed = 0
errors = 0

print(f"开始处理 {total} 个文件...\n")

for i, f in enumerate(files, 1):
    result = process_file(f)
    if result is True:
        changed += 1
        print(f"[{i}/{total}] ✓ {f.name}")
    elif result is False:
        pass
    else:
        errors += 1
        print(f"[{i}/{total}] ✗ {f.name}")
    
    if i % 100 == 0:
        print(f"\n进度: {i}/{total} ({i*100//total}%) | 已修改: {changed} | 错误: {errors}\n")

print(f"\n完成! 总计: {total} | 已修改: {changed} | 无变化: {total-changed-errors} | 错误: {errors}")

